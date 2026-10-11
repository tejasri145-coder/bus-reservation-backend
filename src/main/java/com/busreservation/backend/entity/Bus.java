package com.busreservation.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String busNumber;

    @Column(nullable = false, length = 100)
    private String operatorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BusType busType;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
