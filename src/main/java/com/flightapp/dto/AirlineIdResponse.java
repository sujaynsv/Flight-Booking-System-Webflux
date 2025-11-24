package com.flightapp.dto;

public class AirlineIdResponse {

    private String airlineId;

    public AirlineIdResponse() {
    }

    public AirlineIdResponse(String airlineId) {
        this.airlineId = airlineId;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(String airlineId) {
        this.airlineId = airlineId;
    }
}
