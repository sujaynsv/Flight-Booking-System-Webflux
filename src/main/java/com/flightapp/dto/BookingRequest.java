package com.flightapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.flightapp.model.enums.MealType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {

    @NotBlank
    private String name;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getSeatsCount() {
		return seatsCount;
	}

	public void setSeatsCount(Integer seatsCount) {
		this.seatsCount = seatsCount;
	}

	public List<PassengerRequest> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<PassengerRequest> passengers) {
		this.passengers = passengers;
	}

	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}

	public List<String> getSeatNumbers() {
		return seatNumbers;
	}

	public void setSeatNumbers(List<String> seatNumbers) {
		this.seatNumbers = seatNumbers;
	}

	public LocalDateTime getJourneyDateTime() {
		return journeyDateTime;
	}

	public void setJourneyDateTime(LocalDateTime journeyDateTime) {
		this.journeyDateTime = journeyDateTime;
	}

	@NotBlank
    @Email
    private String emailId;

    @NotNull
    @Positive
    private Integer seatsCount;

    @Valid
    @NotEmpty
    private List<PassengerRequest> passengers;

    @NotNull
    private MealType mealType;

    @NotEmpty
    private List<String> seatNumbers;

    @NotNull
    private LocalDateTime journeyDateTime;

}
