package com.example.reservationservice.service.impl;

import com.example.reservationservice.domain.*;
import com.example.reservationservice.dto.*;
import com.example.reservationservice.mapper.CommentMapper;
import com.example.reservationservice.mapper.ReservationMapper;
import com.example.reservationservice.mapper.TermMapper;
import com.example.reservationservice.repository.CommentRepository;
import com.example.reservationservice.repository.HotelRepository;
import com.example.reservationservice.repository.ReservationRepository;
import com.example.reservationservice.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class ReservationServiceImpl implements ReservationService {

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
    public ReservationListingDTO reservationListing(Long id) {
        return reservationMapper.generateListing(id);
    }

    @Override
    public ReservationDTO makeReservation(CreateReservationDTO dto) {
        Reservation reservation = reservationMapper.createReservationDTOToReservation(dto);
        try {
            jmsTemplate.convertAndSend(destinationCreateReservation, objectMapper.writeValueAsString(reservation.getGuestId()));
            //TODO notification service
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
               //TODO notification service
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
}
