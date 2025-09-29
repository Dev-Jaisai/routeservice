package com.routeservice.feign;

import com.routeservice.dto.TripDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "bus-service",
        url = "http://localhost:8081",  // ✅ Hardcoded URL
        path = "/bus-service/api"
)
public interface BusServiceFeignClient {

    @GetMapping("/trips/available")
    List<TripDTO> getAvailableTrips(
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination,
            @RequestParam("departureDate") String departureDate); // ✅ Added departureDate

    @GetMapping("/trips/route")
    List<TripDTO> getTripsByRoute(
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination);

    @GetMapping("/trips/bus/{busId}")
    List<TripDTO> getTripsByBus(@RequestParam("busId") String busId);
}