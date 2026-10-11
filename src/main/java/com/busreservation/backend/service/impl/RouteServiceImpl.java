package com.busreservation.backend.service.impl;

import com.busreservation.backend.dto.RouteDTO;
import com.busreservation.backend.entity.Route;
import com.busreservation.backend.exception.BadRequestException;
import com.busreservation.backend.exception.ResourceNotFoundException;
import com.busreservation.backend.repository.RouteRepository;
import com.busreservation.backend.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    @Override
    @Transactional
    public RouteDTO createRoute(RouteDTO routeDTO) {
        String source = routeDTO.getSourceCity().trim();
        String destination = routeDTO.getDestinationCity().trim();

        if (source.equalsIgnoreCase(destination)) {
            throw new BadRequestException("Source and destination cities cannot be the same");
        }

        if (routeRepository.findBySourceCityIgnoreCaseAndDestinationCityIgnoreCase(source, destination).isPresent()) {
            throw new BadRequestException("Route from " + source + " to " + destination + " already exists");
        }

        Route route = Route.builder()
                .sourceCity(source)
                .destinationCity(destination)
                .distanceKm(routeDTO.getDistanceKm())
                .estimatedDuration(routeDTO.getEstimatedDuration())
                .build();

        Route saved = routeRepository.save(route);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDTO getRouteById(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
        return mapToDTO(route);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(Long id, RouteDTO routeDTO) {
        Route existing = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));

        String source = routeDTO.getSourceCity().trim();
        String destination = routeDTO.getDestinationCity().trim();

        if (source.equalsIgnoreCase(destination)) {
            throw new BadRequestException("Source and destination cities cannot be the same");
        }

        existing.setSourceCity(source);
        existing.setDestinationCity(destination);
        existing.setDistanceKm(routeDTO.getDistanceKm());
        existing.setEstimatedDuration(routeDTO.getEstimatedDuration());

        Route updated = routeRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteRoute(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Route not found with id: " + id);
        }
        routeRepository.deleteById(id);
    }

    private RouteDTO mapToDTO(Route route) {
        return RouteDTO.builder()
                .id(route.getId())
                .sourceCity(route.getSourceCity())
                .destinationCity(route.getDestinationCity())
                .distanceKm(route.getDistanceKm())
                .estimatedDuration(route.getEstimatedDuration())
                .build();
    }
}
