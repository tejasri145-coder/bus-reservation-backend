package com.busreservation.backend.controller;

import com.busreservation.backend.dto.ApiResponse;
import com.busreservation.backend.dto.RouteDTO;
import com.busreservation.backend.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<ApiResponse<RouteDTO>> createRoute(@Valid @RequestBody RouteDTO routeDTO) {
        RouteDTO created = routeService.createRoute(routeDTO);
        return new ResponseEntity<>(ApiResponse.success("Route created successfully", created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteDTO>> getRouteById(@PathVariable Long id) {
        RouteDTO route = routeService.getRouteById(id);
        return ResponseEntity.ok(ApiResponse.success(route));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getAllRoutes() {
        List<RouteDTO> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(ApiResponse.success(routes));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteDTO>> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteDTO routeDTO) {
        RouteDTO updated = routeService.updateRoute(id, routeDTO);
        return ResponseEntity.ok(ApiResponse.success("Route updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.ok(ApiResponse.success("Route deleted successfully", null));
    }
}
