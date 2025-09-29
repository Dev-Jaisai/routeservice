package com.routeservice.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteSearchResponseDTO {
    private String routeId;
    private String originCity;
    private String destinationCity;
    private Double totalDistance;
    private Double estimatedDuration;
    private List<String> intermediateStops;
    private Integer availableTripsCount;
    // Will be combined with Bus Service trips later
}