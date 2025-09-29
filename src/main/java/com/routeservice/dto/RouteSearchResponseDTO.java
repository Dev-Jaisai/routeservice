package com.routeservice.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteSearchResponseDTO {
    private String routeId;
    private String routeName;
    private String originCity;
    private String destinationCity;
    private Double totalDistance;
    private Double estimatedDuration;
    private List<String> intermediateStops;

    // ✅ Bus Service Data via Feign Client
    private Integer availableTripsCount;
    private List<TripDTO> availableTrips; // Uses TripDTO

    // ✅ Additional combined data
    private Double lowestFare;
    private Double highestFare;
    private String earliestDeparture;
    private String latestDeparture;
    private Integer totalAvailableSeats;
}