package com.example.reservationservice.dto;

import com.example.reservationservice.domain.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationListingDTO {
    List<Reservation> reservationList;
}
