package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Booking;
import com.codelearn.houseselling.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByHouseHouseIdAndBookingDateAndStatusIn(
            Long houseId,
            LocalDate bookingDate,
            List<BookingStatus> statuses
    );

    boolean existsByHouseHouseIdAndBookingDateAndStatusInAndBookingIdNot(
            Long houseId,
            LocalDate bookingDate,
            List<BookingStatus> statuses,
            Long bookingId
    );

    boolean existsByCustomerCustomerId(Long customerId);
}