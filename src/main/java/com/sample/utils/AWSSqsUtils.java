package com.sample.utils;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AWSSqsUtils {

    private static final AmazonSQS client;

    static {
        AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", Regions.AP_SOUTHEAST_2.getName()));
        client = builder.build();
    }

    public static AmazonSQS getClient() {
        return client;
    }

    public static String getSqsQueueUrl() {
        try {
            GetQueueUrlRequest request = new GetQueueUrlRequest("test-queue");
            GetQueueUrlResult response = client.getQueueUrl(request);
            if (response.getQueueUrl() != null) {
                return response.getQueueUrl();
            }
            log.error("Requested queue not yet created");
            return null;
        }
        catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
