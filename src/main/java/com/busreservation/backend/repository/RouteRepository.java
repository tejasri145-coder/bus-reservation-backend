package com.busreservation.backend.repository;

import com.busreservation.backend.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findBySourceCityIgnoreCaseAndDestinationCityIgnoreCase(String sourceCity, String destinationCity);
    List<Route> findBySourceCityIgnoreCase(String sourceCity);
    List<Route> findByDestinationCityIgnoreCase(String destinationCity);
}
