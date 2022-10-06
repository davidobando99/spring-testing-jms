package com.epam.javaprogram.springtesting.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.epam.javaprogram.springtesting.config.JmsConfig;
import com.epam.javaprogram.springtesting.domain.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class BookingServiceIntegrationTest {

    @Spy
    private JmsConfig jmsConfig;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SQSBookingService bookingService;

    @Mock
    private AmazonSQS sqs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    private static final String CONTENT_TYPE = "application/json";

    @Test
    void bookingPostTest() throws Exception {
        Booking booking = new Booking("1","gg","c","ccc",9);

        //given(bookingService.sendBookingMessage(booking,"CREATED"))
         //       .willReturn(booking);
        //PowerMockito.mockStatic(AmazonSQSClientBuilder.class);
        given(AmazonSQSClientBuilder.defaultClient()).willReturn(sqs);


        doReturn(sqs).when(jmsConfig).amazonSQS();

        Mockito.when((sqs.sendMessage(any(SendMessageRequest.class)))).thenReturn(new SendMessageResult());

        this.mockMvc.perform(post("/")
                .content(asJsonString(new Booking("1","gg","c","ccc",9)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
