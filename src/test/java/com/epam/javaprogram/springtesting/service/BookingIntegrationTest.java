package com.epam.javaprogram.springtesting.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.epam.javaprogram.springtesting.domain.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Testcontainers
@SpringBootTest
class BookingIntegrationTest {



    private static final String QUEUE_NAME = "booking-queue";
    private static final String BUCKET_NAME = "booking-bucket";

    @Container
    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.13.0"))
                    .withServices(SQS, S3);


    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", QUEUE_NAME);
        localStack.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
    }

    @DynamicPropertySource
    static void overrideConfiguration(DynamicPropertyRegistry registry) {
        registry.add("event-processing.booking-queue", () -> QUEUE_NAME);
        registry.add("event-processing.booking-bucket", () -> BUCKET_NAME);
        registry.add("cloud.aws.sqs.endpoint", () -> localStack.getEndpointOverride(SQS));
        registry.add("cloud.aws.s3.endpoint", () -> localStack.getEndpointOverride(S3));
        registry.add("cloud.aws.credentials.access-key", localStack::getAccessKey);
        registry.add("cloud.aws.credentials.secret-key", localStack::getSecretKey);
    }

    @Autowired
    private SQSBookingConsumer sqsBookingConsumer;
    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    private AmazonS3 amazonS3;



    @Test
    void bookingPostTest2() throws Exception {
        Booking booking = new Booking("1","gg","c","ccc",9);
        queueMessagingTemplate.convertAndSend(QUEUE_NAME,new GenericMessage<>(booking));


        given()
                .ignoreException(AmazonS3Exception.class)
                .await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> assertNotNull(amazonS3.getObject(BUCKET_NAME, "1")));

    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
