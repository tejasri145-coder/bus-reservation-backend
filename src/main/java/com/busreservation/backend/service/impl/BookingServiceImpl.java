package com.busreservation.backend.service.impl;

import com.busreservation.backend.dto.BookingRequest;
import com.busreservation.backend.dto.BookingResponse;
import com.busreservation.backend.entity.Booking;
import com.busreservation.backend.entity.BookingStatus;
import com.busreservation.backend.entity.Trip;
import com.busreservation.backend.entity.User;
import com.busreservation.backend.exception.BadRequestException;
import com.busreservation.backend.exception.ResourceNotFoundException;
import com.busreservation.backend.repository.BookingRepository;
import com.busreservation.backend.repository.TripRepository;
import com.busreservation.backend.repository.UserRepository;
import com.busreservation.backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + request.getTripId()));

        if (!Boolean.TRUE.equals(trip.getActive())) {
            throw new BadRequestException("This trip is currently inactive and cannot be booked");
        }

        // Validate seats against bus capacity
        int totalBusSeats = trip.getBus().getTotalSeats();
        for (String seat : request.getSeatNumbers()) {
            try {
                // If numeric seat number like "12", validate within bounds
                int seatNum = Integer.parseInt(seat.replaceAll("[^0-9]", ""));
                if (seatNum <= 0 || seatNum > totalBusSeats) {
                    throw new BadRequestException("Seat number " + seat + " is out of valid range (1 - " + totalBusSeats + ")");
                }
            } catch (NumberFormatException ignored) {
                // Non-numeric seat tags allowed as is
            }
        }

        // Check if any seat is already booked for this trip and journey date
        List<String> alreadyBookedSeats = bookingRepository.findBookedSeatsByTripAndDate(trip.getId(), request.getJourneyDate());
        List<String> conflicts = request.getSeatNumbers().stream()
                .filter(alreadyBookedSeats::contains)
                .collect(Collectors.toList());

        if (!conflicts.isEmpty()) {
            throw new BadRequestException("The following seats are already booked for this trip: " + conflicts);
        }

        double totalFare = trip.getFare() * request.getSeatNumbers().size();
        String bookingNumber = generateBookingNumber(request.getJourneyDate());

        Booking booking = Booking.builder()
                .bookingNumber(bookingNumber)
                .user(user)
                .trip(trip)
                .journeyDate(request.getJourneyDate())
                .seatNumbers(request.getSeatNumbers())
                .totalFare(totalFare)
                .status(BookingStatus.CONFIRMED)
                .build();

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingByNumber(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with booking number: " + bookingNumber));
        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getBookedSeats(Long tripId, LocalDate journeyDate) {
        return bookingRepository.findBookedSeatsByTripAndDate(tripId, journeyDate);
    }

    private String generateBookingNumber(LocalDate journeyDate) {
        String datePart = journeyDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "BR-" + datePart + "-" + randomPart;
    }

    private BookingResponse mapToResponse(Booking booking) {
        Trip trip = booking.getTrip();
        User user = booking.getUser();

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .tripId(trip.getId())
                .busNumber(trip.getBus().getBusNumber())
                .busOperator(trip.getBus().getOperatorName())
                .sourceCity(trip.getRoute().getSourceCity())
                .destinationCity(trip.getRoute().getDestinationCity())
                .departureTime(trip.getDepartureTime().toString())
                .arrivalTime(trip.getArrivalTime().toString())
                .journeyDate(booking.getJourneyDate())
                .seatNumbers(booking.getSeatNumbers())
                .totalFare(booking.getTotalFare())
                .status(booking.getStatus())
                .bookingTime(booking.getBookingTime())
                .build();
    }
}
