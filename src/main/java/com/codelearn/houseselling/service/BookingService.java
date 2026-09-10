
        package com.codelearn.houseselling.service;

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

    public Booking createBooking(Booking booking) {

        if (booking.getCustomer() == null ||
                booking.getCustomer().getCustomerId() == null) {

            throw new IllegalArgumentException("Customer is required");
        }

        if (booking.getHouse() == null ||
                booking.getHouse().getHouseId() == null) {

            throw new IllegalArgumentException("House is required");
        }

        Long customerId = booking.getCustomer().getCustomerId();
        Long houseId = booking.getHouse().getHouseId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + customerId
                        ));

        House house = houseRepository.findById(houseId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + houseId
                        ));

        booking.setCustomer(customer);
        booking.setHouse(house);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking updateBooking(Long id, Booking booking) {

        Booking existingBooking = bookingRepository.findById(id)
                .orElse(null);

        if (existingBooking == null) {
            return null;
        }

        if (booking.getCustomer() == null ||
                booking.getCustomer().getCustomerId() == null) {

            throw new IllegalArgumentException("Customer is required");
        }

        if (booking.getHouse() == null ||
                booking.getHouse().getHouseId() == null) {

            throw new IllegalArgumentException("House is required");
        }

        Long customerId = booking.getCustomer().getCustomerId();
        Long houseId = booking.getHouse().getHouseId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + customerId
                        ));

        House house = houseRepository.findById(houseId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + houseId
                        ));

        existingBooking.setBookingDate(booking.getBookingDate());
        existingBooking.setStatus(booking.getStatus());
        existingBooking.setCustomer(customer);
        existingBooking.setHouse(house);

        return bookingRepository.save(existingBooking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}

