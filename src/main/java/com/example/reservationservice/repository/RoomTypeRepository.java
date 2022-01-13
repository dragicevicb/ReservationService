package com.example.reservationservice.repository;

import com.example.reservationservice.domain.Room;
import com.example.reservationservice.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findRoomTypeByRoom(Room room);
}
