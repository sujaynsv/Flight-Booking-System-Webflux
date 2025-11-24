package com.flightapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.PassengerRequest;
import com.flightapp.dto.PnrResponse;
import com.flightapp.exception.BookingNotFoundException;
import com.flightapp.exception.CancellationNotAllowedException;
import com.flightapp.exception.SeatUnavailableException;
import com.flightapp.model.Booking;
import com.flightapp.model.Flight;
import com.flightapp.model.enums.Gender;
import com.flightapp.model.enums.MealType;
import com.flightapp.model.enums.BookingStatus;
import com.flightapp.repository.BookingRepository;
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

class BookingServiceTest {

    private BookingRepository bookingRepository;
    private FlightRepository flightRepository;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        flightRepository = Mockito.mock(FlightRepository.class);
        bookingService = new BookingService(bookingRepository, flightRepository);
    }

    private BookingRequest createValidRequest() {
        BookingRequest request = new BookingRequest();
        request.setName("Sujay");
        request.setEmailId("sujaynsv@gmail.com");
        request.setSeatsCount(1);

        PassengerRequest passenger = new PassengerRequest();
        passenger.setName("Sujay");
        passenger.setGender(Gender.MALE);
        passenger.setAge(23);

        request.setPassengers(Collections.singletonList(passenger));
        request.setMealType(MealType.VEG);
        request.setSeatNumbers(Collections.singletonList("1A"));
        request.setJourneyDateTime(LocalDateTime.now().plusDays(2));
        return request;
    }

    @Test
    void bookTicket_success_shouldReturnPnr() {
        String flightId = "F1";

        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setAvailableSeats(10);
        flight.setOneWayPrice(BigDecimal.valueOf(3500));

        Booking savedBooking = new Booking();
        savedBooking.setPnr("ABC12345");
        savedBooking.setFlightId(flightId);
        savedBooking.setSeatsCount(1);
        savedBooking.setStatus(BookingStatus.BOOKED);
        savedBooking.setJourneyDateTime(LocalDateTime.now().plusDays(2));

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Mono.just(flight));
        Mockito.when(bookingRepository.save(any(Booking.class))).thenReturn(Mono.just(savedBooking));
        Mockito.when(flightRepository.save(any(Flight.class))).thenReturn(Mono.just(flight));

        BookingRequest request = createValidRequest();

        StepVerifier.create(bookingService.bookTicket(flightId, request))
                .assertNext((PnrResponse response) -> {
                    assertThat(response.getPnr()).isEqualTo("ABC12345");
                })
                .verifyComplete();

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        Mockito.verify(bookingRepository, times(1)).save(bookingCaptor.capture());
        Booking saved = bookingCaptor.getValue();
        assertThat(saved.getEmailId()).isEqualTo("sujaynsv@gmail.com");
        assertThat(saved.getSeatNumbers()).containsExactly("1A");
    }

    @Test
    void bookTicket_mismatchedCounts_shouldError() {
        BookingRequest request = createValidRequest();
        request.setSeatsCount(2); // mismatch with lists size 1

        StepVerifier.create(bookingService.bookTicket("F1", request))
                .expectError(IllegalArgumentException.class)
                .verify();

        Mockito.verifyNoInteractions(flightRepository);
        Mockito.verifyNoInteractions(bookingRepository);
    }

    @Test
    void bookTicket_insufficientSeats_shouldError() {
        String flightId = "F1";
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setAvailableSeats(0);

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Mono.just(flight));

        BookingRequest request = createValidRequest();

        StepVerifier.create(bookingService.bookTicket(flightId, request))
                .expectError(SeatUnavailableException.class)
                .verify();
    }

    @Test
    void getTicketByPnr_found_shouldReturnBookingResponse() {
        Booking booking = new Booking();
        booking.setPnr("PNR12345");
        booking.setFlightId("F1");
        booking.setEmailId("sujaynsv@gmail.com");
        booking.setName("Sujay");
        booking.setSeatsCount(1);
        booking.setSeatNumbers(Collections.singletonList("1A"));
        booking.setMealType(MealType.VEG);
        booking.setBookingTime(LocalDateTime.now());
        booking.setJourneyDateTime(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.BOOKED);

        Mockito.when(bookingRepository.findByPnr("PNR12345")).thenReturn(Mono.just(booking));

        StepVerifier.create(bookingService.getTicketByPnr("PNR12345"))
                .assertNext(response -> {
                    assertThat(response.getPnr()).isEqualTo("PNR12345");
                    assertThat(response.getEmailId()).isEqualTo("sujaynsv@gmail.com");
                })
                .verifyComplete();
    }

    @Test
    void getTicketByPnr_notFound_shouldError() {
        Mockito.when(bookingRepository.findByPnr("UNKNOWN")).thenReturn(Mono.empty());

        StepVerifier.create(bookingService.getTicketByPnr("UNKNOWN"))
                .expectError(BookingNotFoundException.class)
                .verify();
    }

    @Test
    void getBookingHistory_shouldReturnFlux() {
        Booking booking = new Booking();
        booking.setPnr("PNR12345");
        booking.setFlightId("F1");
        booking.setEmailId("sujaynsv@gmail.com");
        booking.setName("Sujay");
        booking.setSeatsCount(1);
        booking.setSeatNumbers(Collections.singletonList("1A"));
        booking.setMealType(MealType.VEG);
        booking.setBookingTime(LocalDateTime.now());
        booking.setJourneyDateTime(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.BOOKED);

        Mockito.when(bookingRepository.findByEmailIdOrderByBookingTimeDesc("sujaynsv@gmail.com"))
                .thenReturn(Flux.just(booking));

        StepVerifier.create(bookingService.getBookingHistory("sujaynsv@gmail.com"))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void cancelTicket_before24Hours_shouldCancelAndUpdateSeats() {
        Booking booking = new Booking();
        booking.setPnr("PNR12345");
        booking.setFlightId("F1");
        booking.setEmailId("sujaynsv@gmail.com");
        booking.setSeatsCount(1);
        booking.setJourneyDateTime(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.BOOKED);

        Flight flight = new Flight();
        flight.setId("F1");
        flight.setAvailableSeats(5);

        Mockito.when(bookingRepository.findByPnr("PNR12345")).thenReturn(Mono.just(booking));
        Mockito.when(flightRepository.findById("F1")).thenReturn(Mono.just(flight));
        Mockito.when(flightRepository.save(any(Flight.class))).thenReturn(Mono.just(flight));
        Mockito.when(bookingRepository.save(any(Booking.class))).thenReturn(Mono.just(booking));

        StepVerifier.create(bookingService.cancelTicket("PNR12345"))
                .verifyComplete();

        assertThat(flight.getAvailableSeats()).isEqualTo(6);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void cancelTicket_within24Hours_shouldError() {
        Booking booking = new Booking();
        booking.setPnr("PNR12345");
        booking.setFlightId("F1");
        booking.setSeatsCount(1);
        booking.setJourneyDateTime(LocalDateTime.now().plusHours(10)); // < 24 hours
        booking.setStatus(BookingStatus.BOOKED);

        Mockito.when(bookingRepository.findByPnr("PNR12345")).thenReturn(Mono.just(booking));

        StepVerifier.create(bookingService.cancelTicket("PNR12345"))
                .expectError(CancellationNotAllowedException.class)
                .verify();
    }
}
