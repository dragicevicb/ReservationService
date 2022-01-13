package com.example.reservationservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PriceRequestDTO {
    @NotNull
    private Long id;
}
