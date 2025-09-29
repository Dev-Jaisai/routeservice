package com.routeservice.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "route")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Route {
    @Id
    private String routeId;

    @Column(nullable = false)
    private String routeName;  // "Pusad-Pune Express"

    @Column(nullable = false)
    private String originCity;

    @Column(nullable = false)
    private String destinationCity;

    private BigDecimal totalDistance;  // in kilometers
    private BigDecimal estimatedDuration; // in hours
    private String routeDescription;

    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RouteStop> stops;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}