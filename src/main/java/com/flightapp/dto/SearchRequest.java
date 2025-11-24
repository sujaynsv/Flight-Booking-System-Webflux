package com.flightapp.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SearchRequest {

    @NotBlank
    private String fromPlace;

    @NotBlank
    private String toPlace;

    @NotNull
    @FutureOrPresent
    private LocalDate journeyDate;

    private boolean roundTrip;

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

	public LocalDate getJourneyDate() {
		return journeyDate;
	}

	public void setJourneyDate(LocalDate journeyDate) {
		this.journeyDate = journeyDate;
	}

	public boolean isRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}

}
