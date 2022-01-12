package com.example.reservationservice.repository;

import com.example.reservationservice.domain.Hotel;
import com.example.reservationservice.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomByHotel(Hotel hotel);
    Optional<Room> findRoomById(Long id);
}
