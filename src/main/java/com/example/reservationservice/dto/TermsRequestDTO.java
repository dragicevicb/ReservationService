package com.example.reservationservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class TermsRequestDTO {
    @NotBlank
    public String city;
    @NotBlank
    public String hotelName;
    @NotNull
    public Date startDate;
    @NotNull
    public Date endDate;
}
