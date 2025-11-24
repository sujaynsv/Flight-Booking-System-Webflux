package com.flightapp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flightapp.dto.InventoryRequest;
import com.flightapp.dto.SearchRequest;
import com.flightapp.model.Airline;
import com.flightapp.model.Flight;
import com.flightapp.repository.AirlineRepository;
import com.flightapp.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

class FlightServiceTest {

    private FlightRepository flightRepository;
    private AirlineRepository airlineRepository;
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightRepository = Mockito.mock(FlightRepository.class);
        airlineRepository = Mockito.mock(AirlineRepository.class);
        flightService = new FlightService(flightRepository, airlineRepository);
    }

    @Test
    void addInventory_shouldSaveFlightWithAirlineData() {
        InventoryRequest request = new InventoryRequest();
        request.setAirlineId("AI");
        request.setFromPlace("HYD");
        request.setToPlace("BLR");
        request.setDepartureTime(LocalDateTime.now().plusDays(2));
        request.setArrivalTime(LocalDateTime.now().plusDays(2).plusHours(1));
        request.setOneWayPrice(BigDecimal.valueOf(3500));
        request.setRoundTripPrice(BigDecimal.valueOf(6500));
        request.setTotalSeats(100);

        Airline airline = new Airline("AI", "Air India", "AI");
        Flight savedFlight = new Flight();
        savedFlight.setId("F1");

        Mockito.when(airlineRepository.findById("AI")).thenReturn(Mono.just(airline));
        Mockito.when(flightRepository.save(any(Flight.class))).thenReturn(Mono.just(savedFlight));

        StepVerifier.create(flightService.addInventory(request))
                .expectNextMatches(flight -> "F1".equals(flight.getId()))
                .verifyComplete();

        ArgumentCaptor<Flight> captor = ArgumentCaptor.forClass(Flight.class);
        Mockito.verify(flightRepository, times(1)).save(captor.capture());
        Flight toSave = captor.getValue();
        assertThat(toSave.getAirlineId()).isEqualTo("AI");
        assertThat(toSave.getFromPlace()).isEqualTo("HYD");
        assertThat(toSave.getAvailableSeats()).isEqualTo(100);
    }

    @Test
    void addInventory_airlineNotFound_shouldError() {
        InventoryRequest request = new InventoryRequest();
        request.setAirlineId("UNKNOWN");

        Mockito.when(airlineRepository.findById("UNKNOWN")).thenReturn(Mono.empty());

        StepVerifier.create(flightService.addInventory(request))
                .expectErrorMatches(ex -> ex.getMessage().contains("Airline not found"))
                .verify();
    }

    @Test
    void searchFlights_shouldReturnSearchResults() {
        SearchRequest request = new SearchRequest();
        request.setFromPlace("HYD");
        request.setToPlace("BLR");
        request.setJourneyDate(LocalDate.now().plusDays(2));
        request.setRoundTrip(false);

        Flight flight = new Flight();
        flight.setId("F1");
        flight.setAirlineId("AI");
        flight.setAirlineName("Air India");
        flight.setFromPlace("HYD");
        flight.setToPlace("BLR");
        flight.setDepartureTime(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0));
        flight.setArrivalTime(LocalDateTime.now().plusDays(2).withHour(11).withMinute(30));
        flight.setAvailableSeats(50);
        flight.setOneWayPrice(BigDecimal.valueOf(3500));

        Mockito.when(flightRepository.findByFromPlaceAndToPlaceAndDepartureTimeBetween(
                        anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.just(flight));

        StepVerifier.create(flightService.searchFlights(request))
                .assertNext(result -> {
                    assertThat(result.getFlightId()).isEqualTo("F1");
                    assertThat(result.getAirlineName()).isEqualTo("Air India");
                    assertThat(result.getRemainingSeats()).isEqualTo(50);
                })
                .verifyComplete();
    }
}
