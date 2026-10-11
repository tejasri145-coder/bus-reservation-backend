package com.busreservation.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String sourceCity;

    @Column(nullable = false, length = 100)
    private String destinationCity;

    @Column(nullable = false)
    private Double distanceKm;

    @Column(length = 50)
    private String estimatedDuration;
}
