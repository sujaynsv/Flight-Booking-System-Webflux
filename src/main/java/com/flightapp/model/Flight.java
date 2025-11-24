package com.flightapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Document(collection="flights")
public class Flight {
	
	@Id
	private String id;
	
	@NotBlank
	private String airlineId;
	
	public String getAirlineId() {
		return airlineId;
	}

	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}

	@NotBlank
	private String airlineName;
	
	@NotBlank
	private String fromPlace;
	
	@NotNull
	private LocalDateTime departureTime;
	
	@NotBlank
	private String toPlace;
	
	@NotNull
	private LocalDateTime arrivalTime;
	
	@NotNull
	@PositiveOrZero
	private BigDecimal oneWayPrice;
	
	@PositiveOrZero
	private BigDecimal roundTripPrice;
	
	@NotNull
	@PositiveOrZero
	private Integer totalSeats;
	
	@NotNull
	@PositiveOrZero
	private Integer availableSeats;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public LocalDateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}

	public String getToPlace() {
		return toPlace;
	}

	public void setToPlace(String toPlace) {
		this.toPlace = toPlace;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public BigDecimal getOneWayPrice() {
		return oneWayPrice;
	}

	public void setOneWayPrice(BigDecimal oneWayPrice) {
		this.oneWayPrice = oneWayPrice;
	}

	public BigDecimal getRoundTripPrice() {
		return roundTripPrice;
	}

	public void setRoundTripPrice(BigDecimal roundTripPrice) {
		this.roundTripPrice = roundTripPrice;
	}

	public Integer getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
	}

	public Integer getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(Integer availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	
	
	
	
		
}
