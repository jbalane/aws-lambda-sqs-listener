package com.sample.handler;

import com.sample.model.SQSRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MDHXPOCSampleHandler {

    @Value("#{'${mandatory.fields}'.split(',')}")
    private List<String> mandatoryFields;
    @Value("#{'${optional.fields}'.split(',')}")
    private List<String> optionalFields;
    @Value("#{'${error.fields}'.split(',')}")
    private List<String> errorFields;


    @SqsListener(value = "test-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void listen(SQSRequest message){
        log.info("inside handler");
        log.info("Received message= {}", message);

        //mandatory fields
        log.info("=== Mandatory Fields: START ===");
        for (String field: mandatoryFields) {
            log.info("{}= {}", field, message.getMandatoryFields().get(field));
        }
        log.info("=== Mandatory Fields: END ===");

        //optional fields
        log.info("=== Optional Fields: START ===");
        for (String field: optionalFields) {
            log.info("{}= {}", field, message.getTxnReqInputs().getOptionalFields().get(field));
        }
        log.info("=== Optional Fields: END ===");


        //error fields
        log.info("=== Error Fields: START ===");
        for (String errorField: errorFields) {
            log.info("{}= {}", errorField, message.getMandatoryFields().get(errorField));
        }
        log.info("=== Error Fields: END ===");
        //do something
    }

}
