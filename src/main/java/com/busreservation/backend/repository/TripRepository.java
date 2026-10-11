package com.busreservation.backend.repository;

import com.busreservation.backend.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByActiveTrue();

    List<Trip> findByRouteId(Long routeId);

    List<Trip> findByBusId(Long busId);

    @Query("SELECT t FROM Trip t WHERE LOWER(t.route.sourceCity) = LOWER(:source) " +
           "AND LOWER(t.route.destinationCity) = LOWER(:destination) AND t.active = true")
    List<Trip> searchTrips(@Param("source") String source, @Param("destination") String destination);
}
