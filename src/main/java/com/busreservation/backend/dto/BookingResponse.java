package com.busreservation.backend.dto;

import com.busreservation.backend.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;
    private String bookingNumber;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long tripId;
    private String busNumber;
    private String busOperator;
    private String sourceCity;
    private String destinationCity;
    private String departureTime;
    private String arrivalTime;
    private LocalDate journeyDate;
    private List<String> seatNumbers;
    private Double totalFare;
    private BookingStatus status;
    private LocalDateTime bookingTime;
}
