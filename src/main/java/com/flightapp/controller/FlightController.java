package com.flightapp.controller;

import com.flightapp.dto.FlightIdResponse;
import com.flightapp.dto.InventoryRequest;
import com.flightapp.dto.SearchRequest;
import com.flightapp.dto.SearchResult;
import com.flightapp.model.Flight;
import com.flightapp.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flight")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/airline/inventory")
    public Mono<ResponseEntity<FlightIdResponse>> addInventory(@Valid @RequestBody InventoryRequest request) {
        return flightService.addInventory(request)
                .map(flight -> {
                    FlightIdResponse response = new FlightIdResponse(flight.getId());
                    return ResponseEntity.ok(response);   // 200 OK, only id
                });
    }


    @PostMapping("/search")
    public Flux<SearchResult> searchFlights(@Valid @RequestBody SearchRequest request) {
        return flightService.searchFlights(request);
    }
}
