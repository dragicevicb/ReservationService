package com.example.reservationservice.dto;

import com.example.reservationservice.domain.Room;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CreateHotelDTO {
    @NotNull
    private Long managerId;
    @NotBlank
    private String managerEmail;
    @NotBlank
    private String name;
    @NotBlank
    private String desc;
    @NotBlank
    private String city;
    @NotNull
    private int noOfRooms;
    @NotNull
    private List<Room> rooms;
}
