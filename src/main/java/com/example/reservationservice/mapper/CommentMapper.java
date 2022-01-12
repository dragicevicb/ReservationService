package com.example.reservationservice.mapper;

import com.example.reservationservice.domain.Comment;
import com.example.reservationservice.dto.CommentCreateDTO;
import com.example.reservationservice.dto.CommentDTO;
import com.example.reservationservice.dto.CommentDeleteDTO;
import com.example.reservationservice.dto.ReservationDeleteDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment commentCreateDTOToComment(CommentCreateDTO dto){
        Comment comment = new Comment();
        comment.setGuestId(dto.getGuestId());
        comment.setDesc(dto.getDesc());
        comment.setRating(dto.getRating());
        comment.setHotel(dto.getHotel());
        return comment;
    }

    public CommentDTO commentToCommentDTO(Comment comment){
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setRating(comment.getRating());
        dto.setDesc(comment.getDesc());
        dto.setGuestId(comment.getGuestId());

        return dto;
    }

    public CommentDeleteDTO commentToCommentDeleteDTO(boolean status){
        CommentDeleteDTO dto = new CommentDeleteDTO();
        dto.setStatus(status);

        return dto;
    }
}
