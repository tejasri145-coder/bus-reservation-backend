package com.busreservation.backend.service;

import com.busreservation.backend.dto.TripRequest;
import com.busreservation.backend.dto.TripResponse;

import java.time.LocalDate;
import java.util.List;

public interface TripService {
    TripResponse createTrip(TripRequest tripRequest);
    TripResponse getTripById(Long id);
    List<TripResponse> getAllTrips();
    List<TripResponse> searchTrips(String source, String destination, LocalDate journeyDate);
    TripResponse updateTrip(Long id, TripRequest tripRequest);
    void deleteTrip(Long id);
}
