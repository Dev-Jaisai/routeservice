package com.routeservice.service;

import com.routeservice.dto.RouteDTO;
import com.routeservice.dto.RouteSearchRequestDTO;
import com.routeservice.dto.RouteSearchResponseDTO;
import com.routeservice.dto.RouteStopDTO;

import java.time.LocalDate;
import java.util.List;

public interface RouteService {

    // Route Management
    RouteDTO createRoute(RouteDTO routeDTO);

    RouteDTO getRouteById(String routeId);

    List<RouteDTO> getAllRoutes();

    List<RouteDTO> getRoutesByCities(String originCity, String destinationCity);

    List<RouteDTO> getActiveRoutes();

    RouteDTO updateRoute(String routeId, RouteDTO routeDTO);

    void deleteRoute(String routeId);

    RouteDTO deactivateRoute(String routeId);

    RouteDTO activateRoute(String routeId);

    // Route Search
    List<RouteSearchResponseDTO> searchRoutes(RouteSearchRequestDTO searchRequest);

    List<RouteSearchResponseDTO> searchAvailableRoutes(String originCity, String destinationCity, LocalDate travelDate);

    // Route Stop Management
    RouteStopDTO addStopToRoute(String routeId, RouteStopDTO stopDTO);

    List<RouteStopDTO> getStopsByRoute(String routeId);

    RouteStopDTO updateRouteStop(String stopId, RouteStopDTO stopDTO);

    void removeStopFromRoute(String stopId);

    // Utility Methods
    boolean routeExists(String originCity, String destinationCity);

    RouteDTO getRouteDetails(String originCity, String destinationCity);
}