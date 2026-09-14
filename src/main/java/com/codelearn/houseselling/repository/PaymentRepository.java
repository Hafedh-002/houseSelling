package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Payment;
import com.codelearn.houseselling.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

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

    List<Payment> findByBookingHouseSellerSellerId(
            Long sellerId
    );

    Optional<Payment> findByPaymentIdAndBookingHouseSellerSellerId(
            Long paymentId,
            Long sellerId
    );
}