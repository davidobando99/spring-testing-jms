package com.epam.javaprogram.springtesting.service;

import com.epam.javaprogram.springtesting.domain.Booking;
import com.epam.javaprogram.springtesting.domain.ProcessedBooking;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface BookingConsumer {

    void receiveBookings(Booking booking, String status) throws JsonProcessingException;
}
