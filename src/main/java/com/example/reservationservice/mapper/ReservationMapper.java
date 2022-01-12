package com.example.reservationservice.mapper;

import com.example.reservationservice.domain.Reservation;
import com.example.reservationservice.domain.Room;
import com.example.reservationservice.domain.Term;
import com.example.reservationservice.dto.CreateReservationDTO;
import com.example.reservationservice.dto.ReservationDTO;
import com.example.reservationservice.dto.ReservationDeleteDTO;
import com.example.reservationservice.dto.ReservationListingDTO;
import com.example.reservationservice.repository.ReservationRepository;
import com.example.reservationservice.repository.RoomRepository;
import com.example.reservationservice.repository.TermRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationMapper {

    TermRepository termRepository;
    RoomRepository roomRepository;
    ReservationRepository reservationRepository;

    public ReservationListingDTO generateListing(Long id){
        ReservationListingDTO dto = new ReservationListingDTO();
        dto.setReservationList(reservationRepository.findReservationsById(id));

        return dto;
    }

    public ReservationDeleteDTO reservationToReservationDeleteDTO(boolean status){
        ReservationDeleteDTO dto = new ReservationDeleteDTO();
        dto.setStatus(status);

        return dto;
    }

    public Reservation createReservationDTOToReservation(CreateReservationDTO dto){
        Reservation reservation = new Reservation();
        reservation.setGuestId(dto.getGuestId());
        reservation.setFirstName(dto.getFirstName());
        reservation.setLastName(dto.getLastName());
        reservation.setEmail(dto.getEmail());

        LocalDate startDate = dto.getStartDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = dto.getEndDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().plusDays(1);


        Optional<Room> roomCheck;
        roomCheck = roomRepository.findRoomById(dto.getRoomId());
        Room room = null;
        if(roomCheck.isPresent()){
            room = roomCheck.get();
        }else{
            //shouldn't happen
        }

        Optional<Term> termCheck;
        Term term;
        for(; startDate.isBefore(endDate); startDate = startDate.plusDays(1)){
            Date temp = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            termCheck = termRepository.findTermByDateAndRoom(temp, room);
            if(termCheck.isPresent()){
                term = termCheck.get();
                reservation.getTerms().add(term);
                term.setBooked(true);
            }else{
                //shouldn't happen
            }
        }

        return reservation;
    }

    public ReservationDTO reservationToReservationDTO(Reservation reservation){
        ReservationDTO dto = new ReservationDTO();
        dto.setStatus(true);
        dto.setId(reservation.getId());
        dto.setGuestId(reservation.getGuestId());

        return dto;
    }
}
