package com.flightapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InventoryRequest {

    @NotBlank
    private String airlineId;     

    @NotBlank
    private String fromPlace;

    @NotBlank
    private String toPlace;

    @NotNull
    @Future
    private LocalDateTime departureTime;

    @NotNull
    @Future
    private LocalDateTime arrivalTime;

    @NotNull
    @Positive
    private BigDecimal oneWayPrice;

    private BigDecimal roundTripPrice;

    @NotNull
    @Positive
    private Integer totalSeats;

    public String getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(String airlineId) {
        this.airlineId = airlineId;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
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
}
