package com.flightapp.controller;

import com.flightapp.dto.AirlineIdResponse;
import com.flightapp.model.Airline;
import com.flightapp.repository.AirlineRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flight")
public class AirlineController {

    private final AirlineRepository airlineRepository;

    public AirlineController(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @PostMapping("/airline")
    public Mono<ResponseEntity<AirlineIdResponse>> createAirline(
            @Valid @RequestBody Airline airline) {

        return airlineRepository.save(airline)
                .map(saved -> {
                    AirlineIdResponse response = new AirlineIdResponse(saved.getId());
                    return ResponseEntity.ok(response);   // 200 OK, only airlineId
                });
    }
}
