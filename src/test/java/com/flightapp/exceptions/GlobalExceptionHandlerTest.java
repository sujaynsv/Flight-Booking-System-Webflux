package com.flightapp.exceptions;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;


import com.flightapp.exception.BookingNotFoundException;
import com.flightapp.exception.CancellationNotAllowedException;
import com.flightapp.exception.FlightNotFoundException;
import com.flightapp.exception.GlobalExceptionHandler;
import com.flightapp.exception.SeatUnavailableException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleFlightNotFound_shouldReturn404() {
        FlightNotFoundException ex = new FlightNotFoundException("Flight not found");
        ResponseEntity<String> response = handler.handleFlightNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Flight not found");
    }

    @Test
    void handleBookingNotFound_shouldReturn404() {
        BookingNotFoundException ex = new BookingNotFoundException("Booking not found");
        ResponseEntity<String> response = handler.handleBookingNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Booking not found");
    }

    @Test
    void handleSeatUnavailable_shouldReturn400() {
        SeatUnavailableException ex = new SeatUnavailableException("No seats");
        ResponseEntity<String> response = handler.handleSeatUnavailable(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("No seats");
    }

    @Test
    void handleCancellationNotAllowed_shouldReturn400() {
        CancellationNotAllowedException ex = new CancellationNotAllowedException("Too late");
        ResponseEntity<String> response = handler.handleCancellationNotAllowed(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Too late");
    }
}
