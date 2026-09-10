package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}