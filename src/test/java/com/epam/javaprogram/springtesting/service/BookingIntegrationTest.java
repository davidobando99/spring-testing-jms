package com.epam.javaprogram.springtesting.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.epam.javaprogram.springtesting.domain.Booking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingIntegrationTest {


    private static final String QUEUE_NAME = "booking-queue";
    private static final String BUCKET_NAME = "booking-bucket";


    @Autowired
    private BookingService bookingService;

    @Autowired
    private AmazonS3 amazonS3;

    @Test
    void assertMessageSentSuccessfully(){

        Booking booking = new Booking("1","Altavista","Colombia","Medellin",125);

        Assertions.assertDoesNotThrow(() -> bookingService.sendBookingMessage(booking,"CREATED"));
        Assertions.assertEquals(booking.getId(),bookingService.sendBookingMessage(booking,"CREATED").getId());
    }

    @Test
    void messageShouldBeUploadedToBucketOnceConsumedFromQueue() throws Exception {
        Booking booking = new Booking("2","Cartagena-Hotel","Colombia","Cartagena",900);

        bookingService.sendBookingMessage(booking,"CREATED");
        given()
                .ignoreException(AmazonSQSException.class)
                .await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> assertNotNull(amazonS3.getObject(BUCKET_NAME,"Cartagena-Hotel")));

    }



}
