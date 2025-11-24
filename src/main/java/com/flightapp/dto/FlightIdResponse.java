package com.flightapp.dto;

public class FlightIdResponse {

    private String flightId;

    public FlightIdResponse(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
}
