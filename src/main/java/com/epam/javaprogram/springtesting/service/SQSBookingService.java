package com.epam.javaprogram.springtesting.service;

import com.epam.javaprogram.springtesting.domain.Booking;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service
public class SQSBookingService implements BookingService{

    private static final String BOOKING_QUEUE = "booking-queue";
    private final QueueMessagingTemplate queueMessagingTemplate;

    public SQSBookingService(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    public Booking sendBookingMessage(Booking booking, String status) {

        queueMessagingTemplate.convertAndSend(BOOKING_QUEUE, booking);
        return booking;

    }
}
