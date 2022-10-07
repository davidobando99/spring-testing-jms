package com.epam.javaprogram.springtesting.service;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.javaprogram.springtesting.domain.Booking;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class SQSBookingConsumer implements BookingConsumer{
    private static final Logger LOGGER = LoggerFactory.getLogger(SQSBookingConsumer.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "booking-queue")
    public void receiveBookings(@Payload Booking booking, @Header(name = "status") String status) throws JsonProcessingException {
        LOGGER.info("Message received!");
        LOGGER.info("Message is == " + booking);
        LOGGER.info("Booking status is {}", status);

        amazonS3.putObject("booking-bucket", booking.getName(), objectMapper.writeValueAsString(booking));



    }


}
