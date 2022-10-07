package com.epam.javaprogram.springtesting.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Configuration
@Profile("test")
public class LocalStackConfig {


    @Bean
    public LocalStackContainer localStackContainer() throws IOException, InterruptedException {
        LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.14"))
                .withServices(S3)
                .withEnv("DEFAULT_REGION", "us-east-1");
        localStackContainer.start();
        return localStackContainer;
    }

    @Bean
    public GenericContainer activeMQContainer() throws IOException, InterruptedException {
        GenericContainer activeMQContainer = new GenericContainer<>("rmohr/activemq:latest")
                .withExposedPorts(61616);
        activeMQContainer.start();
        return activeMQContainer;
    }


   /** private static final String QUEUE_NAME = "booking-queue";
    private static final String BUCKET_NAME = "booking-bucket";


    private static final LocalStackContainer.Service[] REQUIRED_SERVICES = {
            LocalStackContainer.Service.S3,
            LocalStackContainer.Service.SQS,
    };

    @Bean
    public LocalStackContainer localStackContainer() throws IOException, InterruptedException {
        LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.13.0"))
                .withServices(SQS, S3);
        localStackContainer.start();
        localStackContainer.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", QUEUE_NAME);
        localStackContainer.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);


        return localStackContainer;
    }**/
}