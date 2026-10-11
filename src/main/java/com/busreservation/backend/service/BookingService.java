package com.busreservation.backend.service;

import com.busreservation.backend.dto.BookingRequest;
import com.busreservation.backend.dto.BookingResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    BookingResponse getBookingById(Long id);
    BookingResponse getBookingByNumber(String bookingNumber);
    List<BookingResponse> getBookingsByUserId(Long userId);
    List<BookingResponse> getAllBookings();
    BookingResponse cancelBooking(Long id);
    List<String> getBookedSeats(Long tripId, LocalDate journeyDate);
}
