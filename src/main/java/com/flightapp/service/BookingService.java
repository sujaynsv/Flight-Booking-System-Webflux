package com.flightapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;
import com.flightapp.dto.PassengerRequest;
import com.flightapp.dto.PnrResponse;
import com.flightapp.exception.BookingNotFoundException;
import com.flightapp.exception.CancellationNotAllowedException;
import com.flightapp.exception.SeatUnavailableException;
import com.flightapp.model.Booking;
import com.flightapp.model.Flight;
import com.flightapp.model.Passenger;
import com.flightapp.model.enums.BookingStatus;
import com.flightapp.repository.BookingRepository;
import com.flightapp.repository.FlightRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingService {

    private static final long CANCELLATION_CUTOFF_HOURS = 24L;

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingService(BookingRepository bookingRepository,
                          FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    // POST /flight/booking/{flightid} -> only PNR in response
    public Mono<PnrResponse> bookTicket(String flightId, BookingRequest request) {
        if (!request.getSeatsCount().equals(request.getPassengers().size())
                || !request.getSeatsCount().equals(request.getSeatNumbers().size())) {
            return Mono.error(new IllegalArgumentException("Seats count, passengers and seat numbers must match"));
        }

        return flightRepository.findById(flightId)
                .switchIfEmpty(Mono.error(new SeatUnavailableException("Flight not found or unavailable")))
                .flatMap(flight -> validateAndCreateBooking(flight, request));
    }

    // internal method now returns PnrResponse as well
    private Mono<PnrResponse> validateAndCreateBooking(Flight flight, BookingRequest request) {
        if (flight.getAvailableSeats() < request.getSeatsCount()) {
            return Mono.error(new SeatUnavailableException("Not enough seats available"));
        }

        String pnr = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime now = LocalDateTime.now();

        List<Passenger> passengers = request.getPassengers().stream()
                .map(this::mapPassenger)
                .collect(Collectors.toList());

        Booking booking = new Booking();
        booking.setPnr(pnr);
        booking.setFlightId(flight.getId());
        booking.setEmailId(request.getEmailId());
        booking.setName(request.getName());
        booking.setSeatsCount(request.getSeatsCount());
        booking.setPassengers(passengers);
        booking.setMealType(request.getMealType());
        booking.setSeatNumbers(request.getSeatNumbers());
        booking.setBookingTime(now);
        booking.setJourneyDateTime(request.getJourneyDateTime());
        booking.setStatus(BookingStatus.BOOKED);

        flight.setAvailableSeats(flight.getAvailableSeats() - request.getSeatsCount());

        return bookingRepository.save(booking)
                .zipWith(flightRepository.save(flight))
                .map(tuple -> new PnrResponse(tuple.getT1().getPnr()));
    }

    private Passenger mapPassenger(PassengerRequest request) {
        return new Passenger(request.getName(), request.getGender(), request.getAge());
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setPnr(booking.getPnr());
        response.setFlightId(booking.getFlightId());
        response.setEmailId(booking.getEmailId());
        response.setName(booking.getName());
        response.setSeatsCount(booking.getSeatsCount());
        response.setSeatNumbers(booking.getSeatNumbers());
        response.setMealType(booking.getMealType());
        response.setBookingTime(booking.getBookingTime());
        response.setJourneyDateTime(booking.getJourneyDateTime());
        response.setStatus(booking.getStatus());
        return response;
    }

    // GET /flight/ticket/{pnr} -> full ticket details
    public Mono<BookingResponse> getTicketByPnr(String pnr) {
        return bookingRepository.findByPnr(pnr)
                .switchIfEmpty(Mono.error(new BookingNotFoundException("Booking not found for PNR: " + pnr)))
                .map(this::mapToResponse);
    }

    // GET /flight/booking/history/{emailId}
    public Flux<BookingResponse> getBookingHistory(String emailId) {
        return bookingRepository.findByEmailIdOrderByBookingTimeDesc(emailId)
                .map(this::mapToResponse);
    }

    // DELETE /flight/booking/cancel/{pnr}
    public Mono<Void> cancelTicket(String pnr) {
        return bookingRepository.findByPnr(pnr)
                .switchIfEmpty(Mono.error(new BookingNotFoundException("Booking not found for PNR: " + pnr)))
                .flatMap(this::cancelIfAllowed)
                .then();
    }

    private Mono<Booking> cancelIfAllowed(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        long hoursDiff = Duration.between(now, booking.getJourneyDateTime()).toHours();

        if (hoursDiff < CANCELLATION_CUTOFF_HOURS) {
            return Mono.error(new CancellationNotAllowedException("Cancellation allowed only before 24 hours"));
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return flightRepository.findById(booking.getFlightId())
                .flatMap(flight -> {
                    flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatsCount());
                    return flightRepository.save(flight);
                })
                .then(bookingRepository.save(booking));
    }
}
