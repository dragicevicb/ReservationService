package com.example.reservationservice.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int pricePerDay;
    @OneToMany
    List<Room> room;
}
