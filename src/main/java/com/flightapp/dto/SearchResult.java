package com.flightapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SearchResult {

    private String flightId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String airlineName;
    private BigDecimal price;
    private Integer remainingSeats;
	public String getFlightId() {
		return flightId;
	}
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}
	public LocalDateTime getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}
	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getAirlineName() {
		return airlineName;
	}
	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getRemainingSeats() {
		return remainingSeats;
	}
	public void setRemainingSeats(Integer remainingSeats) {
		this.remainingSeats = remainingSeats;
	}

}
