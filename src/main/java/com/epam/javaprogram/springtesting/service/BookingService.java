package com.epam.javaprogram.springtesting.service;

import com.epam.javaprogram.springtesting.domain.Booking;
import com.epam.javaprogram.springtesting.domain.ProcessedBooking;

public interface BookingService {


    Booking sendBookingMessage(Booking booking, String status);
}
