package com.busreservation.backend.config;

import com.busreservation.backend.entity.*;
import com.busreservation.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;

    @Override
    public void run(String... args) {
        try {
            initUsers();
            initBusesAndRoutes();
        } catch (Exception e) {
            log.warn("Database initialization skipped or partially applied: {}", e.getMessage());
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@busreservation.com")
                    .password("admin123")
                    .phone("+1234567890")
                    .role(Role.ROLE_ADMIN)
                    .build();

            User user = User.builder()
                    .name("John Doe")
                    .email("john@example.com")
                    .password("user123")
                    .phone("+1987654321")
                    .role(Role.ROLE_USER)
                    .build();

            userRepository.save(admin);
            userRepository.save(user);
            log.info("Initialized default users: admin@busreservation.com and john@example.com");
        }
    }

    private void initBusesAndRoutes() {
        if (busRepository.count() == 0 && routeRepository.count() == 0) {
            Bus bus1 = Bus.builder()
                    .busNumber("BUS-101")
                    .operatorName("Express Lines")
                    .busType(BusType.AC_SLEEPER)
                    .totalSeats(30)
                    .active(true)
                    .build();

            Bus bus2 = Bus.builder()
                    .busNumber("BUS-202")
                    .operatorName("City Rider Travels")
                    .busType(BusType.AC_SEATER)
                    .totalSeats(40)
                    .active(true)
                    .build();

            busRepository.save(bus1);
            busRepository.save(bus2);

            Route route1 = Route.builder()
                    .sourceCity("New York")
                    .destinationCity("Boston")
                    .distanceKm(346.0)
                    .estimatedDuration("4h 30m")
                    .build();

            Route route2 = Route.builder()
                    .sourceCity("Los Angeles")
                    .destinationCity("San Francisco")
                    .distanceKm(615.0)
                    .estimatedDuration("6h 15m")
                    .build();

            routeRepository.save(route1);
            routeRepository.save(route2);

            Trip trip1 = Trip.builder()
                    .bus(bus1)
                    .route(route1)
                    .departureTime(LocalTime.of(8, 0))
                    .arrivalTime(LocalTime.of(12, 30))
                    .fare(45.0)
                    .active(true)
                    .build();

            Trip trip2 = Trip.builder()
                    .bus(bus2)
                    .route(route1)
                    .departureTime(LocalTime.of(15, 30))
                    .arrivalTime(LocalTime.of(20, 0))
                    .fare(38.0)
                    .active(true)
                    .build();

            Trip trip3 = Trip.builder()
                    .bus(bus1)
                    .route(route2)
                    .departureTime(LocalTime.of(9, 0))
                    .arrivalTime(LocalTime.of(15, 15))
                    .fare(55.0)
                    .active(true)
                    .build();

            tripRepository.save(trip1);
            tripRepository.save(trip2);
            tripRepository.save(trip3);

            log.info("Initialized default buses, routes, and trips");
        }
    }
}
