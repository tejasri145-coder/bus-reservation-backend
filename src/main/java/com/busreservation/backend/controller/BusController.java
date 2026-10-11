package com.busreservation.backend.controller;

import com.busreservation.backend.dto.ApiResponse;
import com.busreservation.backend.dto.BusDTO;
import com.busreservation.backend.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bus")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BusController {

    private final BusService busService;

    @PostMapping(value = {"/", ""})
    public ResponseEntity<ApiResponse<BusDTO>> createBus(@Valid @RequestBody BusDTO busDTO) {
        BusDTO created = busService.createBus(busDTO);
        return new ResponseEntity<>(ApiResponse.success("Bus created successfully", created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusDTO>> getBusById(@PathVariable Long id) {
        BusDTO bus = busService.getBusById(id);
        return ResponseEntity.ok(ApiResponse.success(bus));
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<List<BusDTO>> getAllBuses() {
        List<BusDTO> buses = busService.getAllBuses();
        return ResponseEntity.ok(buses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BusDTO>> updateBus(@PathVariable Long id, @Valid @RequestBody BusDTO busDTO) {
        BusDTO updated = busService.updateBus(id, busDTO);
        return ResponseEntity.ok(ApiResponse.success("Bus updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.ok(ApiResponse.success("Bus deleted successfully", null));
    }
}
