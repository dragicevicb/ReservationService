package com.example.reservationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDTO extends DTO{
    private boolean status;
    private Long id;
}
