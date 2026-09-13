package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.PaymentRequest;
import com.codelearn.houseselling.dto.PaymentResponse;
import com.codelearn.houseselling.entity.Booking;
import com.codelearn.houseselling.entity.BookingStatus;
import com.codelearn.houseselling.entity.Payment;
import com.codelearn.houseselling.entity.PaymentStatus;
import com.codelearn.houseselling.repository.BookingRepository;
import com.codelearn.houseselling.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository) {

        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    public PaymentResponse createPayment(
            PaymentRequest request) {

        Booking booking =
                bookingRepository.findById(
                                request.getBookingId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Booking not found with id: "
                                                + request.getBookingId()
                                )
                        );

        // A cancelled booking cannot receive payment.
        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Payment cannot be made for a cancelled booking: "
                            + request.getBookingId()
            );
        }

        // A booking cannot have more than one PAID payment.
        if (request.getStatus() == PaymentStatus.PAID
                && paymentRepository
                .existsByBookingBookingIdAndStatus(
                        request.getBookingId(),
                        PaymentStatus.PAID
                )) {

            throw new IllegalArgumentException(
                    "Booking already has a paid payment: "
                            + request.getBookingId()
            );
        }

        Payment payment = new Payment();

        payment.setAmount(
                request.getAmount()
        );

        payment.setPaymentDate(
                request.getPaymentDate()
        );

        payment.setPaymentMethod(
                request.getPaymentMethod()
        );

        payment.setStatus(
                request.getStatus()
        );

        payment.setBooking(
                booking
        );

        Payment savedPayment =
                paymentRepository.save(payment);

        return convertToResponse(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElse(null);

        if (payment == null) {
            return null;
        }

        return convertToResponse(payment);
    }

    public PaymentResponse updatePayment(
            Long id,
            PaymentRequest request) {

        Payment existingPayment =
                paymentRepository.findById(id)
                        .orElse(null);

        if (existingPayment == null) {
            return null;
        }

        Booking booking =
                bookingRepository.findById(
                                request.getBookingId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Booking not found with id: "
                                                + request.getBookingId()
                                )
                        );

        // A cancelled booking cannot receive payment.
        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Payment cannot be made for a cancelled booking: "
                            + request.getBookingId()
            );
        }

        // Ignore the payment currently being updated.
        if (request.getStatus() == PaymentStatus.PAID
                && paymentRepository
                .existsByBookingBookingIdAndStatusAndPaymentIdNot(
                        request.getBookingId(),
                        PaymentStatus.PAID,
                        id
                )) {

            throw new IllegalArgumentException(
                    "Booking already has a paid payment: "
                            + request.getBookingId()
            );
        }

        existingPayment.setAmount(
                request.getAmount()
        );

        existingPayment.setPaymentDate(
                request.getPaymentDate()
        );

        existingPayment.setPaymentMethod(
                request.getPaymentMethod()
        );

        existingPayment.setStatus(
                request.getStatus()
        );

        existingPayment.setBooking(
                booking
        );

        Payment updatedPayment =
                paymentRepository.save(
                        existingPayment
                );

        return convertToResponse(updatedPayment);
    }

    public void deletePayment(Long id) {

        paymentRepository.deleteById(id);
    }

    private PaymentResponse convertToResponse(
            Payment payment) {

        PaymentResponse response =
                new PaymentResponse();

        response.setPaymentId(
                payment.getPaymentId()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setStatus(
                payment.getStatus()
        );

        if (payment.getBooking() != null) {

            response.setBookingId(
                    payment.getBooking()
                            .getBookingId()
            );

            response.setBookingDate(
                    payment.getBooking()
                            .getBookingDate()
            );

            response.setBookingStatus(
                    payment.getBooking()
                            .getStatus()
                            .name()
            );
        }

        return response;
    }
}