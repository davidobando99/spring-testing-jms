package com.epam.javaprogram.springtesting.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.epam.javaprogram.springtesting.domain.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Testcontainers
@WebMvcTest
class BookingIntegrationTest {



    private static final String QUEUE_NAME = "booking-queue";
    private static final String BUCKET_NAME = "order-event-test-bucket";

    @Container
    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.13.0"))
                    .withServices(SQS);


    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", QUEUE_NAME);
    }

    @DynamicPropertySource
    static void overrideConfiguration(DynamicPropertyRegistry registry) {
        registry.add("event-processing.booking-queue", () -> QUEUE_NAME);
        registry.add("cloud.aws.sqs.endpoint", () -> localStack.getEndpointOverride(SQS));
        registry.add("cloud.aws.credentials.access-key", localStack::getAccessKey);
        registry.add("cloud.aws.credentials.secret-key", localStack::getSecretKey);
    }



    @MockBean
    private BookingService bookingService;

    @MockBean
    private JmsTemplate jmsTemplate;

    @MockBean
    private AmazonSQS amazonSQS;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void bookingPostTest2() throws Exception {
        Booking booking = new Booking("1","gg","c","ccc",9);
        bookingService.sendBookingMessage(booking,"CREATED");

        given()
                .ignoreException(AmazonSQSException.class)
                .await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> assertNotNull(amazonSQS.receiveMessage(String.valueOf(localStack.getEndpointOverride(SQS))).getMessages()));

    }

    @Test
    void bookingPostTest() throws Exception {
        Booking booking = new Booking("1","gg","c","ccc",9);



        this.mockMvc.perform(post("/")
                        .content(asJsonString(new Booking("1","gg","c","ccc",9)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
