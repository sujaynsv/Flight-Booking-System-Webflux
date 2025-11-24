package com.flightapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flightapp.dto.InventoryRequest;
import com.flightapp.dto.SearchRequest;
import com.flightapp.dto.SearchResult;
import com.flightapp.exception.FlightNotFoundException;
import com.flightapp.model.Flight;
import com.flightapp.repository.AirlineRepository;
import com.flightapp.repository.FlightRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;

    public FlightService(FlightRepository flightRepository, AirlineRepository airlineRepository) {
        this.flightRepository = flightRepository;
        this.airlineRepository = airlineRepository;
    }

    public Mono<Flight> addInventory(InventoryRequest request) {
        return airlineRepository.findById(request.getAirlineId())
                .switchIfEmpty(Mono.error(new RuntimeException("Airline not found: " + request.getAirlineId())))
                .flatMap(airline -> {
                    Flight flight = new Flight();
                    flight.setAirlineId(airline.getId());   // NEW
                    flight.setFromPlace(request.getFromPlace());
                    flight.setToPlace(request.getToPlace());
                    flight.setDepartureTime(request.getDepartureTime());
                    flight.setArrivalTime(request.getArrivalTime());
                    flight.setOneWayPrice(request.getOneWayPrice());
                    flight.setRoundTripPrice(request.getRoundTripPrice());
                    flight.setTotalSeats(request.getTotalSeats());
                    flight.setAvailableSeats(request.getTotalSeats());
                    return flightRepository.save(flight);
                });
    }

    public Flux<SearchResult> searchFlights(SearchRequest request) {
        LocalDate date = request.getJourneyDate();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

        return flightRepository
                .findByFromPlaceAndToPlaceAndDepartureTimeBetween(
                        request.getFromPlace(), request.getToPlace(), start, end)
                .map(flight -> {
                    SearchResult result = new SearchResult();
                    result.setFlightId(flight.getId());
                    result.setDepartureTime(flight.getDepartureTime());
                    result.setArrivalTime(flight.getArrivalTime());
                    result.setAirlineName(flight.getAirlineName());
                    result.setPrice(flight.getOneWayPrice());
                    result.setRemainingSeats(flight.getAvailableSeats());
                    return result;
                });
    }

    public Mono<Flight> getFlightById(String flightId) {
        return flightRepository.findById(flightId)
                .switchIfEmpty(Mono.error(new FlightNotFoundException("Flight not found: " + flightId)));
    }
}
