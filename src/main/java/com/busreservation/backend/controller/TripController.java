package com.busreservation.backend.controller;

import com.busreservation.backend.dto.ApiResponse;
import com.busreservation.backend.dto.TripRequest;
import com.busreservation.backend.dto.TripResponse;
import com.busreservation.backend.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<ApiResponse<TripResponse>> createTrip(@Valid @RequestBody TripRequest tripRequest) {
        TripResponse created = tripService.createTrip(tripRequest);
        return new ResponseEntity<>(ApiResponse.success("Trip created successfully", created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> getTripById(@PathVariable Long id) {
        TripResponse trip = tripService.getTripById(id);
        return ResponseEntity.ok(ApiResponse.success(trip));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TripResponse>>> getAllTrips() {
        List<TripResponse> trips = tripService.getAllTrips();
        return ResponseEntity.ok(ApiResponse.success(trips));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TripResponse>>> searchTrips(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TripResponse> trips = tripService.searchTrips(source, destination, date);
        return ResponseEntity.ok(ApiResponse.success(trips));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripRequest tripRequest) {
        TripResponse updated = tripService.updateTrip(id, tripRequest);
        return ResponseEntity.ok(ApiResponse.success("Trip updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok(ApiResponse.success("Trip deleted successfully", null));
    }
}
