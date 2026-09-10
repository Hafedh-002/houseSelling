package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.BookingRequest;
import com.codelearn.houseselling.dto.BookingResponse;
import com.codelearn.houseselling.entity.Booking;
import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.repository.BookingRepository;
import com.codelearn.houseselling.repository.CustomerRepository;
import com.codelearn.houseselling.repository.HouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final HouseRepository houseRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CustomerRepository customerRepository,
            HouseRepository houseRepository) {

        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.houseRepository = houseRepository;
    }

    public BookingResponse createBooking(BookingRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + request.getCustomerId()));

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + request.getHouseId()));

        Booking booking = new Booking();

        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(request.getStatus());
        booking.setCustomer(customer);
        booking.setHouse(house);

        Booking savedBooking = bookingRepository.save(booking);

        return convertToResponse(savedBooking);
    }

    public List<BookingResponse> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public BookingResponse getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id).orElse(null);

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

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + request.getCustomerId()));

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + request.getHouseId()));

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

    private BookingResponse convertToResponse(Booking booking) {

        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getBookingId());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus());

        if (booking.getCustomer() != null) {

            response.setCustomerId(
                    booking.getCustomer().getCustomerId()
            );

            response.setCustomerName(
                    booking.getCustomer().getName()
            );
        }

        if (booking.getHouse() != null) {

            response.setHouseId(
                    booking.getHouse().getHouseId()
            );

            response.setHouseTitle(
                    booking.getHouse().getTitle()
            );
        }

        return response;
    }
}