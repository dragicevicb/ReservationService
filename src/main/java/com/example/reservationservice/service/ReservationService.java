package com.example.reservationservice.service;

import com.example.reservationservice.dto.*;

public interface ReservationService {
    TermsDTO roomListing(TermsRequestDTO dto);
    ReservationListingDTO reservationListing(Long id);
    ReservationDTO makeReservation(CreateReservationDTO dto);
    ReservationDeleteDTO deleteReservation(ReservationDTO dto);
}
