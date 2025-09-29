package com.routeservice.repository;

import com.routeservice.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, String> {

    List<RouteStop> findByRouteRouteId(String routeId);

    List<RouteStop> findByRouteRouteIdAndIsActiveTrue(String routeId);

    List<RouteStop> findByCityName(String cityName);
}