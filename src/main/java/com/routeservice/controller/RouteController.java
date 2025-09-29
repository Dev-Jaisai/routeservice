package com.routeservice.controller;

import com.routeservice.dto.*;
import com.routeservice.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@Slf4j
public class RouteController {

    @Autowired
    private RouteService routeService;

    // Route Management APIs

    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@RequestBody RouteDTO routeDTO) {
        log.info("POST /api/routes - Creating new route: {}", routeDTO.getRouteName());
        RouteDTO createdRoute = routeService.createRoute(routeDTO);
        return ResponseEntity.ok(createdRoute);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable String routeId) {
        log.info("GET /api/routes/{} - Fetching route by ID", routeId);
        RouteDTO route = routeService.getRouteById(routeId);
        return ResponseEntity.ok(route);
    }

    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        log.info("GET /api/routes - Fetching all routes");
        List<RouteDTO> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RouteDTO>> getRoutesByCities(
            @RequestParam String origin,
            @RequestParam String destination) {
        log.info("GET /api/routes/search?origin={}&destination={} - Fetching routes by cities", origin, destination);
        List<RouteDTO> routes = routeService.getRoutesByCities(origin, destination);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/active")
    public ResponseEntity<List<RouteDTO>> getActiveRoutes() {
        log.info("GET /api/routes/active - Fetching active routes");
        List<RouteDTO> routes = routeService.getActiveRoutes();
        return ResponseEntity.ok(routes);
    }

    @PutMapping("/{routeId}")
    public ResponseEntity<RouteDTO> updateRoute(@PathVariable String routeId, @RequestBody RouteDTO routeDTO) {
        log.info("PUT /api/routes/{} - Updating route", routeId);
        RouteDTO updatedRoute = routeService.updateRoute(routeId, routeDTO);
        return ResponseEntity.ok(updatedRoute);
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String routeId) {
        log.info("DELETE /api/routes/{} - Deleting route", routeId);
        routeService.deleteRoute(routeId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{routeId}/deactivate")
    public ResponseEntity<RouteDTO> deactivateRoute(@PathVariable String routeId) {
        log.info("PATCH /api/routes/{}/deactivate - Deactivating route", routeId);
        RouteDTO deactivatedRoute = routeService.deactivateRoute(routeId);
        return ResponseEntity.ok(deactivatedRoute);
    }

    @PatchMapping("/{routeId}/activate")
    public ResponseEntity<RouteDTO> activateRoute(@PathVariable String routeId) {
        log.info("PATCH /api/routes/{}/activate - Activating route", routeId);
        RouteDTO activatedRoute = routeService.activateRoute(routeId);
        return ResponseEntity.ok(activatedRoute);
    }

    // Route Search APIs

    @PostMapping("/search")
    public ResponseEntity<List<RouteSearchResponseDTO>> searchRoutes(@RequestBody RouteSearchRequestDTO searchRequest) {
        log.info("POST /api/routes/search - Searching routes with request: {}", searchRequest);
        List<RouteSearchResponseDTO> routes = routeService.searchRoutes(searchRequest);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/search/available")
    public ResponseEntity<List<RouteSearchResponseDTO>> searchAvailableRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate) {

        log.info("🔍 SEARCH: From {} to {} on {}", origin, destination, travelDate);

        // Use today's date if not provided
        LocalDate searchDate = travelDate != null ? travelDate : LocalDate.now();

        List<RouteSearchResponseDTO> results = routeService.searchAvailableRoutes(origin, destination, searchDate);
        return ResponseEntity.ok(results);
    }
    // Route Stop Management APIs

    @PostMapping("/{routeId}/stops")
    public ResponseEntity<RouteStopDTO> addStopToRoute(@PathVariable String routeId, @RequestBody RouteStopDTO stopDTO) {
        log.info("POST /api/routes/{}/stops - Adding stop to route", routeId);
        RouteStopDTO createdStop = routeService.addStopToRoute(routeId, stopDTO);
        return ResponseEntity.ok(createdStop);
    }

    @GetMapping("/{routeId}/stops")
    public ResponseEntity<List<RouteStopDTO>> getStopsByRoute(@PathVariable String routeId) {
        log.info("GET /api/routes/{}/stops - Fetching stops for route", routeId);
        List<RouteStopDTO> stops = routeService.getStopsByRoute(routeId);
        return ResponseEntity.ok(stops);
    }

    @PutMapping("/stops/{stopId}")
    public ResponseEntity<RouteStopDTO> updateRouteStop(@PathVariable String stopId, @RequestBody RouteStopDTO stopDTO) {
        log.info("PUT /api/routes/stops/{} - Updating route stop", stopId);
        RouteStopDTO updatedStop = routeService.updateRouteStop(stopId, stopDTO);
        return ResponseEntity.ok(updatedStop);
    }

    @DeleteMapping("/stops/{stopId}")
    public ResponseEntity<Void> removeStopFromRoute(@PathVariable String stopId) {
        log.info("DELETE /api/routes/stops/{} - Removing route stop", stopId);
        routeService.removeStopFromRoute(stopId);
        return ResponseEntity.ok().build();
    }

    // Utility APIs

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkRouteExists(
            @RequestParam String origin,
            @RequestParam String destination) {
        log.info("GET /api/routes/exists?origin={}&destination={} - Checking route existence", origin, destination);
        boolean exists = routeService.routeExists(origin, destination);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/details")
    public ResponseEntity<RouteDTO> getRouteDetails(
            @RequestParam String origin,
            @RequestParam String destination) {
        log.info("GET /api/routes/details?origin={}&destination={} - Getting route details", origin, destination);
        RouteDTO route = routeService.getRouteDetails(origin, destination);
        return ResponseEntity.ok(route);
    }
}