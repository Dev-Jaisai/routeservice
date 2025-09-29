package com.routeservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStopDTO {
    private String stopId;
    private String routeId;
    private String cityName;
    private Integer stopSequence;
    private BigDecimal distanceFromOrigin;
    private BigDecimal estimatedStopDuration;
    private String stopType;
    private Boolean isActive;
}