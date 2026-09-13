package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Payment;
import com.codelearn.houseselling.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    boolean existsByBookingBookingIdAndStatus(
            Long bookingId,
            PaymentStatus status
    );

    boolean existsByBookingBookingIdAndStatusAndPaymentIdNot(
            Long bookingId,
            PaymentStatus status,
            Long paymentId
    );
}