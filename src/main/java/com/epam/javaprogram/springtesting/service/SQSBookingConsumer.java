package com.epam.javaprogram.springtesting.service;

import com.epam.javaprogram.springtesting.domain.Booking;
import com.epam.javaprogram.springtesting.domain.ProcessedBooking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class SQSBookingConsumer implements BookingConsumer{
    private static final Logger LOGGER = LoggerFactory.getLogger(SQSBookingConsumer.class);

    @JmsListener(destination = "booking-queue")
    public void receiveBookings(@Payload Booking booking, @Header(name = "status") String status) {
        LOGGER.info("Message received!");
        LOGGER.info("Message is == " + booking);
        LOGGER.info("Booking status is {}", status);

    }


}
