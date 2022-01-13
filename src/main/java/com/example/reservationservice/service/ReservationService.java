package com.example.reservationservice.service;

import com.example.reservationservice.domain.Comment;
import com.example.reservationservice.dto.*;

public interface ReservationService {
    TermsDTO roomListing(TermsRequestDTO dto);
    ReservationListingDTO reservationListing(Long id, String authorization);
    ReservationDTO makeReservation(CreateReservationDTO dto);
    ReservationDeleteDTO deleteReservation(ReservationDTO dto);
    CommentDTO createComment(CommentCreateDTO dto);
    CommentDTO updateComment(CommentCreateDTO dto);
    CommentDeleteDTO deleteComment(CommentDTO dto);
    HotelDTO createHotel(CreateHotelDTO dto);
    HotelDTO updateHotel(CreateHotelDTO dto);
}
