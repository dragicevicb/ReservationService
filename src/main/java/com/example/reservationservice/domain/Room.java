package com.example.reservationservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int roomNo;
    @ManyToOne
    RoomType roomType;
    @ManyToOne
    Hotel hotel;
    @OneToMany
    List<Term> terms;
}
