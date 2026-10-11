package com.busreservation.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {

    private Long id;

    @NotBlank(message = "Source city is required")
    private String sourceCity;

    @NotBlank(message = "Destination city is required")
    private String destinationCity;

    @NotNull(message = "Distance in km is required")
    @Positive(message = "Distance must be positive")
    private Double distanceKm;

    private String estimatedDuration;
}
