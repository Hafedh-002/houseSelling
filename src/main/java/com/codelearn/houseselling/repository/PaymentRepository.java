package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByBookingBookingIdAndStatus(
            Long bookingId,
            String status
    );
}