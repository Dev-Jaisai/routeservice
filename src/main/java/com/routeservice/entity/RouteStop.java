package com.routeservice.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "route_stop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RouteStop {
    @Id
    private String stopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(nullable = false)
    private String cityName;

    @Column(nullable = false)
    private Integer stopSequence;  // 1, 2, 3, etc.

    private BigDecimal distanceFromOrigin; // km from starting point
    private BigDecimal estimatedStopDuration; // minutes to wait at stop
    private String stopType; // "PICKUP", "DROP", "BOTH"

    @Builder.Default
    private Boolean isActive = true;
}