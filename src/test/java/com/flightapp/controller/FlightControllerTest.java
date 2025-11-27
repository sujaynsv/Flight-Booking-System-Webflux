package com.flightapp.controller;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Collections;

import com.flightapp.dto.InventoryRequest;
import com.flightapp.dto.SearchRequest;
import com.flightapp.dto.SearchResult;
import com.flightapp.model.Flight;
import com.flightapp.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class FlightControllerTest {

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        FlightService stubService = new FlightServiceStub();
        FlightController controller = new FlightController(stubService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void addInventory_shouldReturnFlightId_ok() {
        String body = """
                {
                  "airlineId": "AI",
                  "fromPlace": "HYD",
                  "toPlace": "BLR",
                  "departureTime": "2030-01-01T10:00:00",
                  "arrivalTime": "2030-01-01T11:30:00",
                  "oneWayPrice": 3500,
                  "roundTripPrice": 6500,
                  "totalSeats": 100
                }
                """;

        webTestClient.post()
                .uri("/flight/airline/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.flightId").isEqualTo("TEST_FLIGHT");
    }

    @Test
    void searchFlights_shouldReturnResults_ok() {
        String body = """
                {
                  "fromPlace": "HYD",
                  "toPlace": "BLR",
                  "journeyDate": "2030-01-01",
                  "roundTrip": false
                }
                """;

        webTestClient.post()
                .uri("/flight/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SearchResult.class)
                .hasSize(1);
    }

    @Test
    void addInventory_invalidBody_shouldReturnBadRequest() {
        // missing required airlineId, fromPlace, etc. -> validation error
        String body = """
                {
                  "airlineId": "",
                  "fromPlace": "",
                  "toPlace": "",
                  "departureTime": null,
                  "arrivalTime": null,
                  "oneWayPrice": 0,
                  "totalSeats": 0
                }
                """;

        webTestClient.post()
                .uri("/flight/airline/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    // stub service used for controller tests only
    static class FlightServiceStub extends FlightService {

        FlightServiceStub() {
            super(null, null);
        }

        @Override
        public Mono<Flight> addInventory(InventoryRequest request) {
            Flight f = new Flight();
            f.setId("TEST_FLIGHT");
            return Mono.just(f);
        }

        @Override
        public Flux<SearchResult> searchFlights(SearchRequest request) {
            SearchResult r = new SearchResult();
            r.setFlightId("TEST_FLIGHT");
            r.setAirlineName("StubAir");
            r.setDepartureTime(LocalDateTime.of(2030, 1, 1, 10, 0));
            r.setArrivalTime(LocalDateTime.of(2030, 1, 1, 11, 0));
            r.setPrice(BigDecimal.valueOf(3500));
            r.setRemainingSeats(10);
            return Flux.fromIterable(Collections.singletonList(r));
        }
    }
}
