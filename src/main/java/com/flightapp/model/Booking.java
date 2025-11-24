package com.flightapp.model;

import java.time.LocalDateTime;
import java.util.List;

import com.flightapp.model.enums.BookingStatus;
import com.flightapp.model.enums.MealType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    @NotBlank
    private String pnr;

    @NotBlank
    private String flightId;

    @NotBlank
    @Email
    private String emailId;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer seatsCount;

    @Valid
    @NotEmpty
    private List<Passenger> passengers;

    @NotNull
    private MealType mealType;

    @NotEmpty
    private List<String> seatNumbers;

    @NotNull
    private LocalDateTime bookingTime;

    @NotNull
    private LocalDateTime journeyDateTime;

    @NotNull
    private BookingStatus status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeatsCount() {
		return seatsCount;
	}

	public void setSeatsCount(Integer seatsCount) {
		this.seatsCount = seatsCount;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
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

	public LocalDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	public LocalDateTime getJourneyDateTime() {
		return journeyDateTime;
	}

	public void setJourneyDateTime(LocalDateTime journeyDateTime) {
		this.journeyDateTime = journeyDateTime;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

    
}
