package com.example.reservationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelDTO {
    private String name;
    private String desc;
    private String city;
    private int noOfRooms;
}
