package com.example.reservationservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class CreateReservationDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long roomId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotNull
    public Date startDate;
    @NotNull
    public Date endDate;
}
