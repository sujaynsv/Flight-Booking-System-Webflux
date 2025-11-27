package com.flightapp.controller;

import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;
import com.flightapp.dto.PnrResponse;
import com.flightapp.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flight")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/booking/{flightid}")
    public Mono<ResponseEntity<PnrResponse>> bookTicket(
            @PathVariable("flightid") String flightId,
            @Valid @RequestBody BookingRequest request) {
        return bookingService.bookTicket(flightId, request)
                .map(ResponseEntity::ok);
    }


    @GetMapping("/ticket/{pnr}")
    public Mono<ResponseEntity<BookingResponse>> getTicket(@PathVariable("pnr") String pnr) {
        return bookingService.getTicketByPnr(pnr)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/booking/history/{emailId}")
    public Flux<BookingResponse> getHistory(@PathVariable("emailId") String emailId) {
        return bookingService.getBookingHistory(emailId);
    }

    @DeleteMapping("/booking/cancel/{pnr}")
    public Mono<ResponseEntity<Void>> cancelTicket(@PathVariable("pnr") String pnr) {
        return bookingService.cancelTicket(pnr)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
