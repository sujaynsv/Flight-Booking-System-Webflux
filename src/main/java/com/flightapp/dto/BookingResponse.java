package com.flightapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.flightapp.model.enums.BookingStatus;
import com.flightapp.model.enums.MealType;

public class BookingResponse {

    private String pnr;
    private String flightId;
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
	public List<String> getSeatNumbers() {
		return seatNumbers;
	}
	public void setSeatNumbers(List<String> seatNumbers) {
		this.seatNumbers = seatNumbers;
	}
	public MealType getMealType() {
		return mealType;
	}
	public void setMealType(MealType mealType) {
		this.mealType = mealType;
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
	private String emailId;
    private String name;
    private Integer seatsCount;
    private List<String> seatNumbers;
    private MealType mealType;
    private LocalDateTime bookingTime;
    private LocalDateTime journeyDateTime;
    private BookingStatus status;


}
