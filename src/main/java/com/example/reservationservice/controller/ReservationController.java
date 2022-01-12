package com.example.reservationservice.controller;

import com.example.reservationservice.dto.*;
import com.example.reservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReservationListingDTO> reservationListing(@RequestParam(name = "id") Long id){
        return new ResponseEntity<>(reservationService.reservationListing(id), HttpStatus.OK);
    }

    @GetMapping("/listing")
    public ResponseEntity<TermsDTO> roomListing(@RequestBody @Valid TermsRequestDTO dto){
        return new ResponseEntity<>(reservationService.roomListing(dto), HttpStatus.OK);
    }

    @PostMapping("/makeReservation")
    public ResponseEntity<ReservationDTO> makeReservation(@RequestBody @Valid CreateReservationDTO dto){
        return new ResponseEntity<>(reservationService.makeReservation(dto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ReservationDeleteDTO> deleteReservation(@RequestBody @Valid ReservationDTO dto){
        return new ResponseEntity<>(reservationService.deleteReservation(dto), HttpStatus.OK);
    }
}
