package com.routeservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDTO {
    private String tripId;
    private String busId;
    private String busNumber;
    private String operatorName;
    private String busType;
    private String originCity;
    private String destinationCity;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private BigDecimal baseFareAmount;
    private Integer availableSeats;
    private String tripStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Additional fields that might be useful
    private String amenities;
    private Integer totalSeats;
    private String operatorId;
}