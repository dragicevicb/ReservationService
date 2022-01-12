package com.example.reservationservice.repository;

import com.example.reservationservice.domain.Room;
import com.example.reservationservice.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findTermByDateAndRoom(Date date, Room room);
}
