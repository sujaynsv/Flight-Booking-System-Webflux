package com.flightapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class FlightBookingWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightBookingWebfluxApplication.class, args);
	}

}
