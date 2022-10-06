package com.epam.javaprogram.springtesting.service;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.javaprogram.springtesting.domain.Booking;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class SQSBookingConsumer implements BookingConsumer{
    private static final Logger LOGGER = LoggerFactory.getLogger(SQSBookingConsumer.class);

    private final AmazonS3 amazonS3;
    private final ObjectMapper objectMapper;
    private final String bookingBucket;

    public SQSBookingConsumer(AmazonS3 amazonS3, ObjectMapper objectMapper,  @Value("${event-processing.booking-bucket}") String bookingBucket) {
        this.amazonS3 = amazonS3;
        this.objectMapper = objectMapper;
        this.bookingBucket = bookingBucket;
    }

    @SqsListener("booking-queue")
    public void receiveBookings(@Payload Booking booking, @Header(name = "status") String status) throws JsonProcessingException {
        LOGGER.info("Message received!");
        LOGGER.info("Message is == " + booking);
        LOGGER.info("Booking status is {}", status);

        amazonS3.putObject(bookingBucket, booking.getId(), objectMapper.writeValueAsString(booking));

        LOGGER.info("Successfully uploaded order to S3");

    }


}
