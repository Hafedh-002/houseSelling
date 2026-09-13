package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.BookingRequest;
import com.codelearn.houseselling.dto.BookingResponse;
import com.codelearn.houseselling.entity.Booking;
import com.codelearn.houseselling.entity.BookingStatus;
import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.repository.BookingRepository;
import com.codelearn.houseselling.repository.CustomerRepository;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final HouseRepository houseRepository;
    private final SaleRepository saleRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CustomerRepository customerRepository,
            HouseRepository houseRepository,
            SaleRepository saleRepository) {

        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.houseRepository = houseRepository;
        this.saleRepository = saleRepository;
    }

    public BookingResponse createBooking(BookingRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: "
                                        + request.getCustomerId()));

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: "
                                        + request.getHouseId()));

        // A sold house cannot receive a new booking.
        if (saleRepository.existsByHouseHouseIdAndStatus(
                request.getHouseId(),
                "SOLD")) {

            throw new IllegalArgumentException(
                    "House is already sold and cannot be booked: "
                            + request.getHouseId());
        }

        // Only active bookings must be unique
        // for the same house and date.
        if (isActiveStatus(request.getStatus())
                && bookingRepository
                .existsByHouseHouseIdAndBookingDateAndStatusIn(
                        request.getHouseId(),
                        request.getBookingDate(),
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        ))) {

            throw new IllegalArgumentException(
                    "House already has an active booking on this date: "
                            + request.getBookingDate());
        }

        Booking booking = new Booking();

        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(request.getStatus());
        booking.setCustomer(customer);
        booking.setHouse(house);

        Booking savedBooking =
                bookingRepository.save(booking);

        return convertToResponse(savedBooking);
    }

    public List<BookingResponse> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public BookingResponse getBookingById(Long id) {

        Booking booking =
                bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return null;
        }

        return convertToResponse(booking);
    }

    public BookingResponse updateBooking(
            Long id,
            BookingRequest request) {

        Booking existingBooking =
                bookingRepository.findById(id).orElse(null);

        if (existingBooking == null) {
            return null;
        }

        Customer customer =
                customerRepository.findById(request.getCustomerId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()));

        House house =
                houseRepository.findById(request.getHouseId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "House not found with id: "
                                                + request.getHouseId()));

        // A sold house cannot receive a booking.
        if (saleRepository.existsByHouseHouseIdAndStatus(
                request.getHouseId(),
                "SOLD")) {

            throw new IllegalArgumentException(
                    "House is already sold and cannot be booked: "
                            + request.getHouseId());
        }

        // Ignore the booking currently being updated.
        if (isActiveStatus(request.getStatus())
                && bookingRepository
                .existsByHouseHouseIdAndBookingDateAndStatusInAndBookingIdNot(
                        request.getHouseId(),
                        request.getBookingDate(),
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        ),
                        id)) {

            throw new IllegalArgumentException(
                    "House already has an active booking on this date: "
                            + request.getBookingDate());
        }

        existingBooking.setBookingDate(request.getBookingDate());
        existingBooking.setStatus(request.getStatus());
        existingBooking.setCustomer(customer);
        existingBooking.setHouse(house);

        Booking updatedBooking =
                bookingRepository.save(existingBooking);

        return convertToResponse(updatedBooking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private boolean isActiveStatus(BookingStatus status) {

        return status == BookingStatus.PENDING
                || status == BookingStatus.CONFIRMED;
    }

    private BookingResponse convertToResponse(Booking booking) {

        BookingResponse response =
                new BookingResponse();

        response.setBookingId(booking.getBookingId());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus());

        if (booking.getCustomer() != null) {

            response.setCustomerId(
                    booking.getCustomer().getCustomerId());

            response.setCustomerName(
                    booking.getCustomer().getName());
        }

        if (booking.getHouse() != null) {

            response.setHouseId(
                    booking.getHouse().getHouseId());

            response.setHouseTitle(
                    booking.getHouse().getTitle());
        }

        return response;
    }
}