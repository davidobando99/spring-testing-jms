package com.epam.javaprogram.springtesting.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.testcontainers.containers.GenericContainer;

@Profile("test")
@Configuration
public class JmsTestConfig {

    @Autowired
    private GenericContainer activeMQContainer;
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("vm://localhost:" + activeMQContainer.getMappedPort(61616));
        return activeMQConnectionFactory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());

        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ActiveMQConnectionFactory connectionFactory, MessageConverter jacksonJmsMessageConverter){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJmsMessageConverter);
        return factory;
    }

    /**
     private static final GenericContainer activeMQContainer;
     private static final LocalStackContainer localStackContainer;

     static {
     activeMQContainer = new GenericContainer<>("rmohr/activemq:latest")
     .withExposedPorts(61616);

     localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.14"))
     .withServices(S3)
     .withEnv("DEFAULT_REGION", "us-east-1");



     activeMQContainer.start();
     localStackContainer.start();


     }**/

}
