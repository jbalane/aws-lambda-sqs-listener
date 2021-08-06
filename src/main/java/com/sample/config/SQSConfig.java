package com.sample.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

@EnableSqs
@Configuration
@EnableAutoConfiguration
public class SQSConfig {

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(){
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.AP_SOUTHEAST_2)
                .build();
    }

    @Bean
    public QueueMessageHandler queueMessageHandler(AmazonSQSAsync amazonSQSAsync) {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        final ObjectMapper mapper = new ObjectMapper();
        //ignore unknown properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converter.setObjectMapper(mapper);

        factory.setAmazonSqs(amazonSQSAsync);
        factory.setArgumentResolvers(Collections.singletonList(new PayloadMethodArgumentResolver(converter)));
        QueueMessageHandler queueMessageHandler = factory.createQueueMessageHandler();
        return queueMessageHandler;
    }

//    public MappingJackson2MessageConverter jackson2MessageConverter(final ObjectMapper mapper) {
//        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
//        converter.setObjectMapper(mapper);
//        return converter;
//    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

}
