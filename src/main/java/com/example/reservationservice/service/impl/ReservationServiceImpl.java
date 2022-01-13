package com.example.reservationservice.service.impl;

import com.example.reservationservice.domain.*;
import com.example.reservationservice.dto.*;
import com.example.reservationservice.mapper.*;
import com.example.reservationservice.repository.*;
import com.example.reservationservice.service.HttpService;
import com.example.reservationservice.service.ReservationService;
import com.example.reservationservice.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Getter
@Setter
public class ReservationServiceImpl implements ReservationService {

    private TokenService tokenService;
    private HotelMapper hotelMapper;
    private HttpService httpService;
    private RoomTypeRepository roomTypeRepository;
    private RoomRepository roomRepository;
    private PriceMapper priceMapper;
    private HotelRepository hotelRepository;
    private CommentRepository commentRepository;
    private ReservationRepository reservationRepository;
    private TermMapper termMapper;
    private ReservationMapper reservationMapper;
    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper;
    private CommentMapper commentMapper;
    @Value("${destination.createReservation}")
    private String destinationCreateReservation;
    @Value("${destination.deleteReservation}")
    private String destinationDeleteReservation;
    @Value("${destination.sendNotification}")
    private String destinationSendNotification;

    public ReservationServiceImpl(HotelMapper hotelMapper, HttpService httpService, RoomTypeRepository roomTypeRepository,
                                  RoomRepository roomRepository, PriceMapper priceMapper, HotelRepository hotelRepository,
                                  CommentRepository commentRepository, ReservationRepository reservationRepository, TermMapper termMapper,
                                  ReservationMapper reservationMapper, JmsTemplate jmsTemplate, ObjectMapper objectMapper, CommentMapper commentMapper) {
        this.hotelMapper = hotelMapper;
        this.httpService = httpService;
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.priceMapper = priceMapper;
        this.hotelRepository = hotelRepository;
        this.commentRepository = commentRepository;
        this.reservationRepository = reservationRepository;
        this.termMapper = termMapper;
        this.reservationMapper = reservationMapper;
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.commentMapper = commentMapper;
    }

    //jako ruzan kod, valjalo bi popraviti
    @Override
    public TermsDTO roomListing(TermsRequestDTO dto) {
        List<Room> availableRooms = new ArrayList<>();
        Optional<Hotel> hotel = hotelRepository.findHotelByNameAndCity(dto.getHotelName(),dto.getCity());
        if(hotel.isPresent()){
            List<Room> roomList = hotel.get().getRooms();
            for(Room room : roomList){
                boolean isRoomAvailable = true;
                List<Term> terms = room.getTerms();
                LocalDate startDate = dto.getStartDate().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate endDate = dto.getEndDate().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate().plusDays(1);
                for(; startDate.isBefore(endDate); startDate = startDate.plusDays(1)){
                    boolean flag = false;
                    for(Term term : terms){
                        LocalDate temp = term.getDate().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        if(temp.equals(startDate) && !term.isBooked()){
                            flag = true;
                        }
                    }
                    if(!flag){
                        isRoomAvailable = false;
                    }
                }
                if(isRoomAvailable){
                    availableRooms.add(room);
                }
            }
            return termMapper.generateListing(availableRooms);
        }else{
            return null;
        }
    }

    @Override
    public ReservationListingDTO reservationListing(Long id, String authorization) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String type = claims.get("type", String.class);
        return reservationMapper.generateListing(id, type);
    }

    @Override
    public ReservationDTO makeReservation(CreateReservationDTO dto) {
        Reservation reservation = reservationMapper.createReservationDTOToReservation(dto);
        int discount = 0;

        long diff = dto.getEndDate().getTime() - dto.getStartDate().getTime();
        long difference_In_Days
                = (diff
                / (1000 * 60 * 60 * 24))
                % 365;
        PriceRequestDTO requestDTO = priceMapper.generateRequest(reservation.getGuestId());

        try {
            discount = httpService.getDiscount(requestDTO);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        double price = roomTypeRepository.findRoomTypeByRoom(roomRepository.getById(dto.getRoomId()))
                .get().getPricePerDay()*difference_In_Days*((100.0-discount)/100.0);

        reservation.setPrice(price);
        try {
            jmsTemplate.convertAndSend(destinationCreateReservation, objectMapper.writeValueAsString(reservation.getGuestId()));
            Map<String,String> map = new HashMap<>();
            map.put("firstName", reservation.getFirstName());
            map.put("lastName",reservation.getLastName());
            map.put("price", String.valueOf(reservation.getPrice()));
            map.put("roomNo", String.valueOf(roomRepository.getById(dto.getRoomId()).getRoomNo()));
            List<String> recipients = new ArrayList<>();
            recipients.add(reservation.getEmail());
            recipients.add(roomRepository.getById(dto.getRoomId()).getHotel().getManagerEmail());
            CreateNotificationDTO notification = new CreateNotificationDTO(reservation.getGuestId(), recipients,
                                                map, "makeReservation");
            jmsTemplate.convertAndSend(destinationDeleteReservation, objectMapper.writeValueAsString(notification));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Hotel hotel = roomRepository.getById(dto.getRoomId()).getHotel();
        hotel.getReservations().add(reservation);
        hotelRepository.save(hotel);
        reservationRepository.save(reservation);
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @Override
    public ReservationDeleteDTO deleteReservation(ReservationDTO dto) {
       Optional<Reservation> reservationCheck = reservationRepository.findReservationById(dto.getId());
       if(reservationCheck.isPresent()){
           Reservation reservation = reservationCheck.get();
           for(Term t : reservation.getTerms()){
               t.setBooked(false);
           }
           try {
               jmsTemplate.convertAndSend(destinationDeleteReservation, objectMapper.writeValueAsString(reservation.getGuestId()));
               Map<String,String> map = new HashMap<>();
               map.put("firstName", reservation.getFirstName());
               map.put("lastName",reservation.getLastName());
               map.put("roomNo",String.valueOf(reservation.getTerms().get(1).getRoom().getRoomNo()));
               List<String> recipients = new ArrayList<>();
               recipients.add(reservation.getEmail());
               recipients.add(roomRepository.getById(reservation.getTerms().get(1).getRoom().getId()).getHotel().getManagerEmail());
               CreateNotificationDTO notification = new CreateNotificationDTO(reservation.getGuestId(), recipients,
                                                        map, "deleteReservation");
               jmsTemplate.convertAndSend(destinationDeleteReservation, objectMapper.writeValueAsString(notification));
           }catch (Exception ex){
               ex.printStackTrace();
           }

           reservationRepository.delete(reservation);
           return reservationMapper.reservationToReservationDeleteDTO(true);
       }
        return reservationMapper.reservationToReservationDeleteDTO(false);
    }

    @Override
    public CommentDTO createComment(CommentCreateDTO dto) {
        Comment comment = commentMapper.commentCreateDTOToComment(dto);
        commentRepository.save(comment);

        return commentMapper.commentToCommentDTO(comment);
    }

    @Override
    public CommentDTO updateComment(CommentCreateDTO dto) {
        Comment comment = commentMapper.commentCreateDTOToComment(dto);
        commentRepository.save(comment);

        return commentMapper.commentToCommentDTO(comment);
    }

    @Override
    public CommentDeleteDTO deleteComment(CommentDTO dto) {
        if(commentRepository.findCommentById(dto.getId()).isPresent()){
            commentRepository.deleteById(dto.getId());
            return commentMapper.commentToCommentDeleteDTO(true);
        }

        return commentMapper.commentToCommentDeleteDTO(false);
    }

    @Override
    public HotelDTO createHotel(CreateHotelDTO dto) {
        Hotel hotel = hotelMapper.createHotelDTOTOHotel(dto);
        hotelRepository.save(hotel);

        return hotelMapper.hotelToHotelDTO(hotel);
    }

    @Override
    public HotelDTO updateHotel(CreateHotelDTO dto) {
        Hotel hotel = hotelMapper.createHotelDTOTOHotel(dto);
        hotelRepository.save(hotel);

        return hotelMapper.hotelToHotelDTO(hotel);
    }
}
