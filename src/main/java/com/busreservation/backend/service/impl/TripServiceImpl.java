package com.busreservation.backend.service.impl;

import com.busreservation.backend.dto.BusDTO;
import com.busreservation.backend.dto.RouteDTO;
import com.busreservation.backend.dto.TripRequest;
import com.busreservation.backend.dto.TripResponse;
import com.busreservation.backend.entity.Bus;
import com.busreservation.backend.entity.Route;
import com.busreservation.backend.entity.Trip;
import com.busreservation.backend.exception.ResourceNotFoundException;
import com.busreservation.backend.repository.BookingRepository;
import com.busreservation.backend.repository.BusRepository;
import com.busreservation.backend.repository.RouteRepository;
import com.busreservation.backend.repository.TripRepository;
import com.busreservation.backend.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public TripResponse createTrip(TripRequest tripRequest) {
        Bus bus = busRepository.findById(tripRequest.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + tripRequest.getBusId()));

        Route route = routeRepository.findById(tripRequest.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + tripRequest.getRouteId()));

        Trip trip = Trip.builder()
                .bus(bus)
                .route(route)
                .departureTime(tripRequest.getDepartureTime())
                .arrivalTime(tripRequest.getArrivalTime())
                .fare(tripRequest.getFare())
                .active(tripRequest.getActive() != null ? tripRequest.getActive() : true)
                .build();

        Trip saved = tripRepository.save(trip);
        return mapToResponse(saved, null);
    }

    @Override
    @Transactional(readOnly = true)
    public TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));
        return mapToResponse(trip, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponse> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(trip -> mapToResponse(trip, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponse> searchTrips(String source, String destination, LocalDate journeyDate) {
        List<Trip> trips = tripRepository.searchTrips(source.trim(), destination.trim());
        return trips.stream()
                .map(trip -> mapToResponse(trip, journeyDate))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TripResponse updateTrip(Long id, TripRequest tripRequest) {
        Trip existing = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

        Bus bus = busRepository.findById(tripRequest.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + tripRequest.getBusId()));

        Route route = routeRepository.findById(tripRequest.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + tripRequest.getRouteId()));

        existing.setBus(bus);
        existing.setRoute(route);
        existing.setDepartureTime(tripRequest.getDepartureTime());
        existing.setArrivalTime(tripRequest.getArrivalTime());
        existing.setFare(tripRequest.getFare());
        if (tripRequest.getActive() != null) {
            existing.setActive(tripRequest.getActive());
        }

        Trip updated = tripRepository.save(existing);
        return mapToResponse(updated, null);
    }

    @Override
    @Transactional
    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trip not found with id: " + id);
        }
        tripRepository.deleteById(id);
    }

    private TripResponse mapToResponse(Trip trip, LocalDate journeyDate) {
        int availableSeats = trip.getBus().getTotalSeats();
        if (journeyDate != null) {
            List<String> bookedSeats = bookingRepository.findBookedSeatsByTripAndDate(trip.getId(), journeyDate);
            availableSeats = Math.max(0, trip.getBus().getTotalSeats() - bookedSeats.size());
        }

        Bus bus = trip.getBus();
        BusDTO busDTO = BusDTO.builder()
                .id(bus.getId())
                .busNumber(bus.getBusNumber())
                .operatorName(bus.getOperatorName())
                .busType(bus.getBusType())
                .totalSeats(bus.getTotalSeats())
                .active(bus.getActive())
                .build();

        Route route = trip.getRoute();
        RouteDTO routeDTO = RouteDTO.builder()
                .id(route.getId())
                .sourceCity(route.getSourceCity())
                .destinationCity(route.getDestinationCity())
                .distanceKm(route.getDistanceKm())
                .estimatedDuration(route.getEstimatedDuration())
                .build();

        return TripResponse.builder()
                .id(trip.getId())
                .bus(busDTO)
                .route(routeDTO)
                .departureTime(trip.getDepartureTime())
                .arrivalTime(trip.getArrivalTime())
                .fare(trip.getFare())
                .active(trip.getActive())
                .availableSeatsCount(availableSeats)
                .build();
    }
}
