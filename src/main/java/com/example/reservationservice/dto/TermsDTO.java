package com.example.reservationservice.dto;

import com.example.reservationservice.domain.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TermsDTO {
    List<Room> rooms;
}
