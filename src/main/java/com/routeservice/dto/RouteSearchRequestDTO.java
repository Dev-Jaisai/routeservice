package com.routeservice.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteSearchRequestDTO {
    private String originCity;
    private String destinationCity;
    private LocalDate travelDate;
    private Integer passengers;
}