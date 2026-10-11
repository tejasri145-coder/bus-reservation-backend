package com.busreservation.backend.service;

import com.busreservation.backend.dto.RouteDTO;

import java.util.List;

public interface RouteService {
    RouteDTO createRoute(RouteDTO routeDTO);
    RouteDTO getRouteById(Long id);
    List<RouteDTO> getAllRoutes();
    RouteDTO updateRoute(Long id, RouteDTO routeDTO);
    void deleteRoute(Long id);
}
