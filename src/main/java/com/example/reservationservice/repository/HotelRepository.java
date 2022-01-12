package com.example.reservationservice.repository;

import com.example.reservationservice.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findHotelByNameAndCity(String name,String city);
    List<Hotel> findHotelsByName(String name);
    List<Hotel> findHotelsByCity(String city);
}
