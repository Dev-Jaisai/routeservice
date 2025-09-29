package com.routeservice.repository;

import com.routeservice.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {

    List<Route> findByOriginCityAndDestinationCity(String originCity, String destinationCity);

    List<Route> findByOriginCityAndDestinationCityAndIsActiveTrue(String originCity, String destinationCity);

    List<Route> findByOriginCity(String originCity);

    List<Route> findByDestinationCity(String destinationCity);

    List<Route> findByIsActiveTrue();

    Optional<Route> findByRouteName(String routeName);

    boolean existsByOriginCityAndDestinationCity(String originCity, String destinationCity);

    @Query("SELECT r FROM Route r WHERE LOWER(r.originCity) = LOWER(:originCity) AND LOWER(r.destinationCity) = LOWER(:destinationCity)")
    List<Route> findByOriginAndDestinationIgnoreCase(@Param("originCity") String originCity,
                                                     @Param("destinationCity") String destinationCity);
}