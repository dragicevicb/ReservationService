package com.example.reservationservice.mapper;

import com.example.reservationservice.domain.Room;
import com.example.reservationservice.dto.TermsDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TermMapper {
    public TermsDTO generateListing(List<Room> availableRooms){
        TermsDTO dto = new TermsDTO();
        dto.setRooms(availableRooms);

        return dto;
    }
}
