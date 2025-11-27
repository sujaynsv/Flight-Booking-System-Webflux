package com.flightapp.controller;

import java.time.LocalDateTime;
import java.util.Collections;

import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;
import com.flightapp.dto.PnrResponse;

import com.flightapp.model.enums.MealType;
import com.flightapp.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class BookingControllerTest {

    private WebTestClient webTestClient;
    private BookingServiceStub stubService;

    @BeforeEach
    void setUp() {
        stubService = new BookingServiceStub();
        BookingController controller = new BookingController(stubService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void bookTicket_shouldReturnPnr_ok() {
        String body = """
                {
                  "name": "Sujay",
                  "emailId": "sujaynsv@gmail.com",
                  "seatsCount": 1,
                  "passengers": [
                    { "name": "Sujay", "gender": "MALE", "age": 23 }
                  ],
                  "mealType": "VEG",
                  "seatNumbers": ["1A"],
                  "journeyDateTime": "2030-01-01T10:00:00"
                }
                """;

        webTestClient.post()
                .uri("/flight/booking/F1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PnrResponse.class)
                .value(resp -> org.assertj.core.api.Assertions
                        .assertThat(resp.getPnr()).isEqualTo("TESTPNR"));
    }

    @Test
    void bookTicket_invalidRequest_shouldReturnBadRequest() {
        // seatsCount 1 but empty passengers -> triggers validation / IllegalArgumentException in real service,
        // here we just assert 4xx from controller binding
        String body = """
                {
                  "name": "",
                  "emailId": "bad-email",
                  "seatsCount": 1,
                  "passengers": [],
                  "mealType": "VEG",
                  "seatNumbers": [],
                  "journeyDateTime": null
                }
                """;

        webTestClient.post()
                .uri("/flight/booking/F1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getTicket_shouldReturnBooking_ok() {
        webTestClient.get()
                .uri("/flight/ticket/TESTPNR")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookingResponse.class)
                .value(resp -> org.assertj.core.api.Assertions
                        .assertThat(resp.getPnr()).isEqualTo("TESTPNR"));
    }

    @Test
    void bookingHistory_shouldReturnList_ok() {
        webTestClient.get()
                .uri("/flight/booking/history/someone@example.com")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookingResponse.class)
                .hasSize(1);
    }

    @Test
    void cancelTicket_shouldReturnNoContent_ok() {
        webTestClient.delete()
                .uri("/flight/booking/cancel/TESTPNR")
                .exchange()
                .expectStatus().isNoContent();
    }

    // stub service used only for controller tests
    static class BookingServiceStub extends BookingService {

        private final BookingResponse sampleResponse;

        BookingServiceStub() {
            super(null, null);
            sampleResponse = new BookingResponse();
            sampleResponse.setPnr("TESTPNR");
            sampleResponse.setFlightId("F1");
            sampleResponse.setEmailId("sujaynsv@gmail.com");
            sampleResponse.setName("Sujay");
            sampleResponse.setSeatsCount(1);
            sampleResponse.setSeatNumbers(Collections.singletonList("1A"));
            sampleResponse.setMealType(MealType.VEG);
            sampleResponse.setBookingTime(LocalDateTime.of(2030, 1, 1, 9, 0));
            sampleResponse.setJourneyDateTime(LocalDateTime.of(2030, 1, 1, 10, 0));
        }

        @Override
        public Mono<PnrResponse> bookTicket(String flightId, BookingRequest request) {
            return Mono.just(new PnrResponse("TESTPNR"));
        }

        @Override
        public Mono<BookingResponse> getTicketByPnr(String pnr) {
            return Mono.just(sampleResponse);
        }

        @Override
        public Flux<BookingResponse> getBookingHistory(String emailId) {
            return Flux.fromIterable(Collections.singletonList(sampleResponse));
        }

        @Override
        public Mono<Void> cancelTicket(String pnr) {
            return Mono.empty();
        }
    }
}
