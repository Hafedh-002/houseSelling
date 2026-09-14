package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.BookingRequest;
import com.codelearn.houseselling.dto.BookingResponse;
import com.codelearn.houseselling.entity.Booking;
import com.codelearn.houseselling.entity.BookingStatus;
import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.BookingRepository;
import com.codelearn.houseselling.repository.CustomerRepository;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SaleRepository;
import com.codelearn.houseselling.repository.SellerRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final HouseRepository houseRepository;
    private final SaleRepository saleRepository;
    private final SellerRepository sellerRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CustomerRepository customerRepository,
            HouseRepository houseRepository,
            SaleRepository saleRepository,
            SellerRepository sellerRepository) {

        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.houseRepository = houseRepository;
        this.saleRepository = saleRepository;
        this.sellerRepository = sellerRepository;
    }

    public BookingResponse createBooking(
            BookingRequest request) {

        Seller seller = getLoggedInSeller();

        Customer customer =
                customerRepository.findById(
                                request.getCustomerId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()
                                )
                        );

        House house =
                houseRepository.findById(
                                request.getHouseId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "House not found with id: "
                                                + request.getHouseId()
                                )
                        );

        // Seller can only create bookings
        // for houses they own.
        if (house.getSeller() == null
                || !house.getSeller()
                .getSellerId()
                .equals(seller.getSellerId())) {

            throw new AccessDeniedException(
                    "You cannot create a booking for another seller's house"
            );
        }

        // Sold house cannot receive new bookings.
        if (saleRepository
                .existsByHouseHouseIdAndStatus(
                        request.getHouseId(),
                        "SOLD"
                )) {

            throw new IllegalArgumentException(
                    "House is already sold and cannot be booked: "
                            + request.getHouseId()
            );
        }

        // Prevent multiple active bookings
        // for same house and date.
        if (isActiveStatus(request.getStatus())
                && bookingRepository
                .existsByHouseHouseIdAndBookingDateAndStatusIn(
                        request.getHouseId(),
                        request.getBookingDate(),
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        )
                )) {

            throw new IllegalArgumentException(
                    "House already has an active booking on this date: "
                            + request.getBookingDate()
            );
        }

        Booking booking = new Booking();

        booking.setBookingDate(
                request.getBookingDate()
        );

        booking.setStatus(
                request.getStatus()
        );

        booking.setCustomer(
                customer
        );

        booking.setHouse(
                house
        );

        Booking savedBooking =
                bookingRepository.save(booking);

        return convertToResponse(savedBooking);
    }

    public List<BookingResponse> getAllBookings() {

        Seller seller = getLoggedInSeller();

        return bookingRepository
                .findByHouseSellerSellerId(
                        seller.getSellerId()
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public BookingResponse getBookingById(
            Long id) {

        Seller seller = getLoggedInSeller();

        Booking booking =
                bookingRepository
                        .findByBookingIdAndHouseSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (booking == null) {
            return null;
        }

        return convertToResponse(booking);
    }

    public BookingResponse updateBooking(
            Long id,
            BookingRequest request) {

        Seller seller = getLoggedInSeller();

        Booking existingBooking =
                bookingRepository
                        .findByBookingIdAndHouseSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (existingBooking == null) {
            return null;
        }

        Customer customer =
                customerRepository.findById(
                                request.getCustomerId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()
                                )
                        );

        House house =
                houseRepository.findById(
                                request.getHouseId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "House not found with id: "
                                                + request.getHouseId()
                                )
                        );

        // Seller cannot move a booking
        // to another seller's house.
        if (house.getSeller() == null
                || !house.getSeller()
                .getSellerId()
                .equals(seller.getSellerId())) {

            throw new AccessDeniedException(
                    "You cannot use another seller's house"
            );
        }

        if (saleRepository
                .existsByHouseHouseIdAndStatus(
                        request.getHouseId(),
                        "SOLD"
                )) {

            throw new IllegalArgumentException(
                    "House is already sold and cannot be booked: "
                            + request.getHouseId()
            );
        }

        if (isActiveStatus(request.getStatus())
                && bookingRepository
                .existsByHouseHouseIdAndBookingDateAndStatusInAndBookingIdNot(
                        request.getHouseId(),
                        request.getBookingDate(),
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        ),
                        id
                )) {

            throw new IllegalArgumentException(
                    "House already has an active booking on this date: "
                            + request.getBookingDate()
            );
        }

        existingBooking.setBookingDate(
                request.getBookingDate()
        );

        existingBooking.setStatus(
                request.getStatus()
        );

        existingBooking.setCustomer(
                customer
        );

        existingBooking.setHouse(
                house
        );

        Booking updatedBooking =
                bookingRepository.save(
                        existingBooking
                );

        return convertToResponse(updatedBooking);
    }

    public boolean deleteBooking(
            Long id) {

        Seller seller = getLoggedInSeller();

        Booking booking =
                bookingRepository
                        .findByBookingIdAndHouseSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (booking == null) {
            return false;
        }

        bookingRepository.delete(booking);

        return true;
    }

    private Seller getLoggedInSeller() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "Seller is not authenticated"
            );
        }

        String email =
                authentication.getName();

        return sellerRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Logged-in seller not found"
                        )
                );
    }

    private boolean isActiveStatus(
            BookingStatus status) {

        return status == BookingStatus.PENDING
                || status == BookingStatus.CONFIRMED;
    }

    private BookingResponse convertToResponse(
            Booking booking) {

        BookingResponse response =
                new BookingResponse();

        response.setBookingId(
                booking.getBookingId()
        );

        response.setBookingDate(
                booking.getBookingDate()
        );

        response.setStatus(
                booking.getStatus()
        );

        if (booking.getCustomer() != null) {

            response.setCustomerId(
                    booking.getCustomer()
                            .getCustomerId()
            );

            response.setCustomerName(
                    booking.getCustomer()
                            .getName()
            );
        }

        if (booking.getHouse() != null) {

            response.setHouseId(
                    booking.getHouse()
                            .getHouseId()
            );

            response.setHouseTitle(
                    booking.getHouse()
                            .getTitle()
            );
        }

        return response;
    }
}