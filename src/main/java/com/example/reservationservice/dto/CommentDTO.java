package com.example.reservationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO extends DTO{
    private Long id;
    private int rating;
    private String desc;
}
