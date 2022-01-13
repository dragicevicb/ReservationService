package com.example.reservationservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long managerId;
    private String managerEmail;
    private String name;
    private String desc;
    private String city;
    private int noOfRooms;
    @OneToMany()
    List<Room> rooms;
    @OneToMany
    List<Reservation> reservations;
}
