
        package com.codelearn.houseselling.service;

import com.codelearn.houseselling.entity.Booking;
import com.codelearn.houseselling.entity.Payment;
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

    public Payment createPayment(Payment payment) {

        if (payment.getBooking() == null ||
                payment.getBooking().getBookingId() == null) {

            throw new IllegalArgumentException("Booking is required");
        }

        Long bookingId = payment.getBooking().getBookingId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found with id: " + bookingId
                        ));

        payment.setBooking(booking);

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment updatePayment(Long id, Payment payment) {

        Payment existingPayment = paymentRepository.findById(id)
                .orElse(null);

        if (existingPayment == null) {
            return null;
        }

        if (payment.getBooking() == null ||
                payment.getBooking().getBookingId() == null) {

            throw new IllegalArgumentException("Booking is required");
        }

        Long bookingId = payment.getBooking().getBookingId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found with id: " + bookingId
                        ));

        existingPayment.setAmount(payment.getAmount());
        existingPayment.setPaymentDate(payment.getPaymentDate());
        existingPayment.setPaymentMethod(payment.getPaymentMethod());
        existingPayment.setStatus(payment.getStatus());
        existingPayment.setBooking(booking);

        return paymentRepository.save(existingPayment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
