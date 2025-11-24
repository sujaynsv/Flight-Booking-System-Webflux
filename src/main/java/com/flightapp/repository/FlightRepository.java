package com.flightapp.repository;

import java.time.LocalDateTime;

import com.flightapp.model.Flight;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FlightRepository extends ReactiveMongoRepository<Flight, String> {

    Flux<Flight> findByFromPlaceAndToPlaceAndDepartureTimeBetween(
            String fromPlace,
            String toPlace,
            LocalDateTime start,
            LocalDateTime end);
}
