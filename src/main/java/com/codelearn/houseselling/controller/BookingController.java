package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.dto.BookingRequest;
import com.codelearn.houseselling.dto.BookingResponse;
import com.codelearn.houseselling.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request) {

        BookingResponse response =
                bookingService.createBooking(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id) {

        BookingResponse response =
                bookingService.getBookingById(id);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequest request) {

        BookingResponse response =
                bookingService.updateBooking(id, request);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }
}