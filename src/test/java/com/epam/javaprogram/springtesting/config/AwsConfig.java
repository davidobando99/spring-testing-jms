package com.epam.javaprogram.springtesting.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Profile("test")
@EnableJms
@Configuration
public class AwsConfig {

    private static final String QUEUE_NAME = "booking-queue";
    private static final String BUCKET_NAME = "booking-bucket";



    @Autowired
    private LocalStackContainer localStackContainer;

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                localStackContainer.getEndpointOverride(S3).toString(),
                                localStackContainer.getRegion()
                        )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey())
                        )
                )
                .build();

        if (!amazonS3.doesBucketExistV2("booking-bucket")) {
            amazonS3.createBucket(new CreateBucketRequest("booking-bucket"));
        }

        return amazonS3;
    }
    /**

        @Autowired
        private LocalStackContainer localStackContainer;

        @Bean
        public AmazonS3 amazonS3() {
            return AmazonS3ClientBuilder
                    .standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(
                                    localStackContainer.getEndpointOverride(S3).toString(),
                                    localStackContainer.getRegion()
                            )
                    )
                    .withCredentials(
                            new AWSStaticCredentialsProvider(
                                    new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey())
                            )
                    )
                    .build();
        }

        @Bean
        public AmazonSQS amazonSQS() {
            return AmazonSQSClientBuilder
                    .standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(
                                    localStackContainer.getEndpointOverride(SQS).toString(),
                                    localStackContainer.getRegion()
                            )
                    )
                    .withCredentials(
                            new AWSStaticCredentialsProvider(
                                    new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey())
                            )
                    )
                    .build();
        }




        @Bean
        public SQSConnectionFactory connectionFactory()  {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                    localStackContainer.getAccessKey(),
                    localStackContainer.getSecretKey());
            return new SQSConnectionFactory(new ProviderConfiguration(),
                    SqsClient.builder()
                            .region(Region.of(localStackContainer.getRegion()))
                            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                            .build());
        }

        @Bean
        public MessageConverter jacksonJmsMessageConverter(){
            MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
            converter.setTargetType(MessageType.TEXT);
            converter.setTypeIdPropertyName("_type");
            return converter;
        }

        @Bean
        public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(SQSConnectionFactory connectionFactory, MessageConverter jacksonJmsMessageConverter){
            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(jacksonJmsMessageConverter);
            return factory;
        }

        @Bean
        public JmsTemplate jmsTemplate()  {
            System.out.println(connectionFactory());
            JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
            jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
            jmsTemplate.setDeliveryPersistent(true);
            jmsTemplate.setSessionTransacted(false);
            return jmsTemplate;

        }
**/
    }

