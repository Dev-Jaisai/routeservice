package com.routeservice.service.impl;

import com.routeservice.dto.*;
import com.routeservice.entity.Route;
import com.routeservice.entity.RouteStop;
import com.routeservice.repository.RouteRepository;
import com.routeservice.repository.RouteStopRepository;
import com.routeservice.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteStopRepository routeStopRepository;

    @Override
    public RouteDTO createRoute(RouteDTO routeDTO) {
        log.info("Creating new route: {} from {} to {}",
                routeDTO.getRouteName(), routeDTO.getOriginCity(), routeDTO.getDestinationCity());

        // Check if route already exists
        if (routeRepository.existsByOriginCityAndDestinationCity(
                routeDTO.getOriginCity(), routeDTO.getDestinationCity())) {
            log.error("Route already exists from {} to {}", routeDTO.getOriginCity(), routeDTO.getDestinationCity());
            throw new RuntimeException("Route already exists between these cities");
        }

        Route route = convertToRouteEntity(routeDTO);
        Route savedRoute = routeRepository.save(route);

        // Save stops if provided
        if (routeDTO.getStops() != null && !routeDTO.getStops().isEmpty()) {
            List<RouteStop> stops = routeDTO.getStops().stream()
                    .map(stopDTO -> convertToStopEntity(stopDTO, savedRoute))
                    .collect(Collectors.toList());
            routeStopRepository.saveAll(stops);
        }

        log.info("Route created successfully with ID: {}", savedRoute.getRouteId());
        return convertToRouteDTO(savedRoute);
    }

    @Override
    public RouteDTO getRouteById(String routeId) {
        log.debug("Fetching route by ID: {}", routeId);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    log.error("Route not found with ID: {}", routeId);
                    return new RuntimeException("Route not found with ID: " + routeId);
                });

        return convertToRouteDTO(route);
    }

    @Override
    public List<RouteDTO> getAllRoutes() {
        log.debug("Fetching all routes");

        List<Route> routes = routeRepository.findAll();
        log.info("Found {} routes", routes.size());

        return routes.stream()
                .map(this::convertToRouteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteDTO> getRoutesByCities(String originCity, String destinationCity) {
        log.debug("Fetching routes from {} to {}", originCity, destinationCity);

        List<Route> routes = routeRepository.findByOriginCityAndDestinationCity(originCity, destinationCity);
        log.info("Found {} routes from {} to {}", routes.size(), originCity, destinationCity);

        return routes.stream()
                .map(this::convertToRouteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteDTO> getActiveRoutes() {
        log.debug("Fetching all active routes");

        List<Route> routes = routeRepository.findByIsActiveTrue();
        log.info("Found {} active routes", routes.size());

        return routes.stream()
                .map(this::convertToRouteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteDTO updateRoute(String routeId, RouteDTO routeDTO) {
        log.info("Updating route with ID: {}", routeId);

        Route existingRoute = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    log.error("Route not found for update with ID: {}", routeId);
                    return new RuntimeException("Route not found with ID: " + routeId);
                });

        // Update fields
        if (routeDTO.getRouteName() != null) {
            existingRoute.setRouteName(routeDTO.getRouteName());
        }
        if (routeDTO.getTotalDistance() != null) {
            existingRoute.setTotalDistance(routeDTO.getTotalDistance());
        }
        if (routeDTO.getEstimatedDuration() != null) {
            existingRoute.setEstimatedDuration(routeDTO.getEstimatedDuration());
        }
        if (routeDTO.getRouteDescription() != null) {
            existingRoute.setRouteDescription(routeDTO.getRouteDescription());
        }
        if (routeDTO.getIsActive() != null) {
            existingRoute.setIsActive(routeDTO.getIsActive());
        }

        Route updatedRoute = routeRepository.save(existingRoute);
        log.info("Route updated successfully with ID: {}", routeId);

        return convertToRouteDTO(updatedRoute);
    }

    @Override
    public void deleteRoute(String routeId) {
        log.info("Deleting route with ID: {}", routeId);

        if (!routeRepository.existsById(routeId)) {
            log.error("Route not found for deletion with ID: {}", routeId);
            throw new RuntimeException("Route not found with ID: " + routeId);
        }

        routeRepository.deleteById(routeId);
        log.info("Route deleted successfully with ID: {}", routeId);
    }

    @Override
    public RouteDTO deactivateRoute(String routeId) {
        log.info("Deactivating route with ID: {}", routeId);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    log.error("Route not found for deactivation with ID: {}", routeId);
                    return new RuntimeException("Route not found with ID: " + routeId);
                });

        route.setIsActive(false);
        Route deactivatedRoute = routeRepository.save(route);
        log.info("Route deactivated successfully with ID: {}", routeId);

        return convertToRouteDTO(deactivatedRoute);
    }

    @Override
    public RouteDTO activateRoute(String routeId) {
        log.info("Activating route with ID: {}", routeId);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    log.error("Route not found for activation with ID: {}", routeId);
                    return new RuntimeException("Route not found with ID: " + routeId);
                });

        route.setIsActive(true);
        Route activatedRoute = routeRepository.save(route);
        log.info("Route activated successfully with ID: {}", routeId);

        return convertToRouteDTO(activatedRoute);
    }

    @Override
    public List<RouteSearchResponseDTO> searchRoutes(RouteSearchRequestDTO searchRequest) {
        log.info("Searching routes from {} to {} for {} passengers",
                searchRequest.getOriginCity(), searchRequest.getDestinationCity(), searchRequest.getPassengers());

        List<Route> routes = routeRepository.findByOriginCityAndDestinationCityAndIsActiveTrue(
                searchRequest.getOriginCity().toUpperCase(),
                searchRequest.getDestinationCity().toUpperCase()
        );

        return routes.stream()
                .map(this::convertToSearchResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteSearchResponseDTO> searchAvailableRoutes(String originCity, String destinationCity) {
        log.info("Searching available routes from {} to {}", originCity, destinationCity);

        List<Route> routes = routeRepository.findByOriginCityAndDestinationCityAndIsActiveTrue(
                originCity.toUpperCase(),
                destinationCity.toUpperCase()
        );

        return routes.stream()
                .map(this::convertToSearchResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteStopDTO addStopToRoute(String routeId, RouteStopDTO stopDTO) {
        log.info("Adding stop to route: {}", routeId);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    log.error("Route not found with ID: {}", routeId);
                    return new RuntimeException("Route not found with ID: " + routeId);
                });

        RouteStop stop = convertToStopEntity(stopDTO, route);
        RouteStop savedStop = routeStopRepository.save(stop);

        log.info("Stop added successfully with ID: {}", savedStop.getStopId());
        return convertToStopDTO(savedStop);
    }

    @Override
    public List<RouteStopDTO> getStopsByRoute(String routeId) {
        log.debug("Fetching stops for route: {}", routeId);

        List<RouteStop> stops = routeStopRepository.findByRouteRouteIdAndIsActiveTrue(routeId);
        log.info("Found {} stops for route: {}", stops.size(), routeId);

        return stops.stream()
                .map(this::convertToStopDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteStopDTO updateRouteStop(String stopId, RouteStopDTO stopDTO) {
        log.info("Updating route stop: {}", stopId);

        RouteStop existingStop = routeStopRepository.findById(stopId)
                .orElseThrow(() -> {
                    log.error("Route stop not found with ID: {}", stopId);
                    return new RuntimeException("Route stop not found with ID: " + stopId);
                });

        // Update fields
        if (stopDTO.getCityName() != null) {
            existingStop.setCityName(stopDTO.getCityName());
        }
        if (stopDTO.getStopSequence() != null) {
            existingStop.setStopSequence(stopDTO.getStopSequence());
        }
        if (stopDTO.getDistanceFromOrigin() != null) {
            existingStop.setDistanceFromOrigin(stopDTO.getDistanceFromOrigin());
        }
        if (stopDTO.getEstimatedStopDuration() != null) {
            existingStop.setEstimatedStopDuration(stopDTO.getEstimatedStopDuration());
        }
        if (stopDTO.getStopType() != null) {
            existingStop.setStopType(stopDTO.getStopType());
        }
        if (stopDTO.getIsActive() != null) {
            existingStop.setIsActive(stopDTO.getIsActive());
        }

        RouteStop updatedStop = routeStopRepository.save(existingStop);
        log.info("Route stop updated successfully with ID: {}", stopId);

        return convertToStopDTO(updatedStop);
    }

    @Override
    public void removeStopFromRoute(String stopId) {
        log.info("Removing route stop: {}", stopId);

        if (!routeStopRepository.existsById(stopId)) {
            log.error("Route stop not found with ID: {}", stopId);
            throw new RuntimeException("Route stop not found with ID: " + stopId);
        }

        routeStopRepository.deleteById(stopId);
        log.info("Route stop removed successfully with ID: {}", stopId);
    }

    @Override
    public boolean routeExists(String originCity, String destinationCity) {
        log.debug("Checking if route exists from {} to {}", originCity, destinationCity);
        return routeRepository.existsByOriginCityAndDestinationCity(originCity, destinationCity);
    }

    @Override
    public RouteDTO getRouteDetails(String originCity, String destinationCity) {
        log.debug("Getting route details from {} to {}", originCity, destinationCity);

        List<Route> routes = routeRepository.findByOriginCityAndDestinationCity(originCity, destinationCity);
        if (routes.isEmpty()) {
            log.error("No route found from {} to {}", originCity, destinationCity);
            throw new RuntimeException("No route found between specified cities");
        }

        // Return the first active route, or first one if no active routes
        Route route = routes.stream()
                .filter(Route::getIsActive)
                .findFirst()
                .orElse(routes.get(0));

        return convertToRouteDTO(route);
    }

    // Conversion methods
    private Route convertToRouteEntity(RouteDTO routeDTO) {
        return Route.builder()
                .routeId(routeDTO.getRouteId())
                .routeName(routeDTO.getRouteName())
                .originCity(routeDTO.getOriginCity().toUpperCase())
                .destinationCity(routeDTO.getDestinationCity().toUpperCase())
                .totalDistance(routeDTO.getTotalDistance())
                .estimatedDuration(routeDTO.getEstimatedDuration())
                .routeDescription(routeDTO.getRouteDescription())
                .isActive(routeDTO.getIsActive())
                .createdAt(routeDTO.getCreatedAt())
                .updatedAt(routeDTO.getUpdatedAt())
                .build();
    }

    private RouteDTO convertToRouteDTO(Route route) {
        List<RouteStopDTO> stopDTOs = route.getStops() != null ?
                route.getStops().stream()
                        .map(this::convertToStopDTO)
                        .collect(Collectors.toList()) :
                null;

        return RouteDTO.builder()
                .routeId(route.getRouteId())
                .routeName(route.getRouteName())
                .originCity(route.getOriginCity())
                .destinationCity(route.getDestinationCity())
                .totalDistance(route.getTotalDistance())
                .estimatedDuration(route.getEstimatedDuration())
                .routeDescription(route.getRouteDescription())
                .isActive(route.getIsActive())
                .stops(stopDTOs)
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .build();
    }

    private RouteStop convertToStopEntity(RouteStopDTO stopDTO, Route route) {
        return RouteStop.builder()
                .stopId(stopDTO.getStopId())
                .route(route)
                .cityName(stopDTO.getCityName().toUpperCase())
                .stopSequence(stopDTO.getStopSequence())
                .distanceFromOrigin(stopDTO.getDistanceFromOrigin())
                .estimatedStopDuration(stopDTO.getEstimatedStopDuration())
                .stopType(stopDTO.getStopType())
                .isActive(stopDTO.getIsActive())
                .build();
    }

    private RouteStopDTO convertToStopDTO(RouteStop stop) {
        return RouteStopDTO.builder()
                .stopId(stop.getStopId())
                .routeId(stop.getRoute().getRouteId())
                .cityName(stop.getCityName())
                .stopSequence(stop.getStopSequence())
                .distanceFromOrigin(stop.getDistanceFromOrigin())
                .estimatedStopDuration(stop.getEstimatedStopDuration())
                .stopType(stop.getStopType())
                .isActive(stop.getIsActive())
                .build();
    }

    private RouteSearchResponseDTO convertToSearchResponseDTO(Route route) {
        List<String> intermediateStops = route.getStops() != null ?
                route.getStops().stream()
                        .filter(RouteStop::getIsActive)
                        .map(RouteStop::getCityName)
                        .collect(Collectors.toList()) :
                List.of();

        return RouteSearchResponseDTO.builder()
                .routeId(route.getRouteId())
                .originCity(route.getOriginCity())
                .destinationCity(route.getDestinationCity())
                .totalDistance(route.getTotalDistance() != null ? route.getTotalDistance().doubleValue() : 0.0)
                .estimatedDuration(route.getEstimatedDuration() != null ? route.getEstimatedDuration().doubleValue() : 0.0)
                .intermediateStops(intermediateStops)
                .availableTripsCount(0) // Will be populated when integrated with Bus Service
                .build();
    }
}