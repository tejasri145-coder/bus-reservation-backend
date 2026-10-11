package com.busreservation.backend.dto;

import com.busreservation.backend.entity.BusType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusDTO {

    private Long id;

    @NotBlank(message = "Bus number is required")
    private String busNumber;

    @NotBlank(message = "Operator name is required")
    private String operatorName;

    @NotNull(message = "Bus type is required")
    private BusType busType;

    @NotNull(message = "Total seats is required")
    @Min(value = 10, message = "Total seats must be at least 10")
    private Integer totalSeats;

    private Boolean active;
}
