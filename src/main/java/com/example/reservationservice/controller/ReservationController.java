package com.example.reservationservice.controller;

import com.example.reservationservice.dto.*;
import com.example.reservationservice.security.CheckSecurity;
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
    public ResponseEntity<ReservationListingDTO> reservationListing(@PathVariable("id") Long id,
                                                                    @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.reservationListing(id, authorization), HttpStatus.OK);
    }

    @GetMapping("/listing")
    public ResponseEntity<TermsDTO> roomListing(@RequestBody @Valid TermsRequestDTO dto){
        return new ResponseEntity<>(reservationService.roomListing(dto), HttpStatus.OK);
    }

    @PostMapping("/makeReservation")
    @CheckSecurity(roles = {"C","M"})
    public ResponseEntity<ReservationDTO> makeReservation(@RequestBody @Valid CreateReservationDTO dto,
                                                          @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.makeReservation(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteReservation")
    @CheckSecurity(roles = {"C","M"})
    public ResponseEntity<ReservationDeleteDTO> deleteReservation(@RequestBody @Valid ReservationDTO dto,
                                                                  @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.deleteReservation(dto), HttpStatus.OK);
    }

    @PostMapping("/makeComment")
    @CheckSecurity(roles = {"C","M"})
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentCreateDTO dto,
                                                    @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.createComment(dto), HttpStatus.CREATED);
    }

    @PutMapping("/updateComment")
    @CheckSecurity(roles = {"C","M"})
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentCreateDTO dto,
                                                    @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.updateComment(dto), HttpStatus.OK);
    }

    @DeleteMapping("/deleteComment")
    @CheckSecurity(roles = {"C","M"})
    public ResponseEntity<CommentDeleteDTO> deleteComment(@RequestBody CommentDTO dto,
                                                          @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.deleteComment(dto), HttpStatus.OK);
    }

    @PostMapping("/createHotel")
    @CheckSecurity(roles = {"A","M"})
    public ResponseEntity<HotelDTO> createHotel(@RequestBody CreateHotelDTO dto,
                                                @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.createHotel(dto),HttpStatus.CREATED);
    }

    @PutMapping("/updateHotel")
    @CheckSecurity(roles = {"A","M"})
    public ResponseEntity<HotelDTO> updateHotel(@RequestBody CreateHotelDTO dto,
                                                @RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.createHotel(dto),HttpStatus.OK);
    }
}
