package com.routeservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDTO {
    private String routeId;
    private String routeName;
    private String originCity;
    private String destinationCity;
    private BigDecimal totalDistance;
    private BigDecimal estimatedDuration;
    private String routeDescription;
    private Boolean isActive;
    private List<RouteStopDTO> stops;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}