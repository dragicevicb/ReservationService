package com.example.reservationservice.mapper;

import com.example.reservationservice.domain.Comment;
import com.example.reservationservice.dto.CommentCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment commentCreateDTOToComment(CommentCreateDTO dto){
        Comment comment = new Comment();
    }
}
