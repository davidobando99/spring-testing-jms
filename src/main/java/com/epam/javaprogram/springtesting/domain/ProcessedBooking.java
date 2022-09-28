package com.epam.javaprogram.springtesting.domain;

public class ProcessedBooking {

    private Booking bookingInfo;

    private String bookingStatus;

    public ProcessedBooking(Booking bookingInfo, String bookingStatus) {
        this.bookingInfo = bookingInfo;
        this.bookingStatus = bookingStatus;
    }

    public Booking getBookingInfo() {
        return bookingInfo;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }
}
