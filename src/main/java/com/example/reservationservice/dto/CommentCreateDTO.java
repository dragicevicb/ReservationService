package com.example.reservationservice.dto;

import com.example.reservationservice.domain.Comment;
import com.example.reservationservice.domain.Hotel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentCreateDTO extends DTO{
    @NotNull
    @Min(1)
    @Max(5)
    private int rating;
    @NotNull
    private String desc;
    @NotNull
    Hotel hotel;
}
