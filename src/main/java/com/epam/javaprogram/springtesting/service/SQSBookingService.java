package com.epam.javaprogram.springtesting.service;

import com.epam.javaprogram.springtesting.domain.Booking;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service
public class SQSBookingService implements BookingService{

    private static final String BOOKING_QUEUE = "booking-queue";
    private final JmsTemplate jmsTemplate;

    public SQSBookingService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Booking sendBookingMessage(Booking booking, String status) {

        jmsTemplate.convertAndSend(BOOKING_QUEUE, booking, message -> {
            message.setStringProperty("status", status);
            return message;
        });

        return booking;

    }
}
