package com.busreservation.backend.repository;

import com.busreservation.backend.entity.Booking;
import com.busreservation.backend.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByTripIdAndJourneyDateAndStatus(Long tripId, LocalDate journeyDate, BookingStatus status);

    @Query("SELECT s FROM Booking b JOIN b.seatNumbers s WHERE b.trip.id = :tripId " +
           "AND b.journeyDate = :journeyDate AND b.status = 'CONFIRMED'")
    List<String> findBookedSeatsByTripAndDate(@Param("tripId") Long tripId, @Param("journeyDate") LocalDate journeyDate);
}
