import cloud.localstack.ServiceName;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.sample.utils.AWSSqsUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest (classes = MDHXPOCSampleApplication.class)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = { ServiceName.SQS }, useSingleDockerContainer = true)
public class MDHXPOCSampleApplicationTest {

    private static final String QUEUE_NAME = "test-queue";
    private static final String PAYLOAD = "{\"xmlS3Url\": \"s3://test/sample.xml\", \"jsonUrl\": \"s3://test/sample.json\"}";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @BeforeAll
    public static void setUp() {

        CreateQueueRequest queueRequest = new CreateQueueRequest(QUEUE_NAME);
        CreateQueueResult queueResult = AWSSqsUtils.getClient().createQueue(queueRequest);
        if (queueResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            log.info("Queue successfully created {}", queueResult.getQueueUrl());
        }

    }

    @Test
    public void testHandler(){

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("contentType", "application/json");

        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("contentType", "application/json");

//        Message<String> payload = MessageBuilder.withPayload(PAYLOAD).setHeader("contentType", "application/json").build();

        Message payload = new Message();
        payload.setBody(PAYLOAD);
        payload.setAttributes(attributes);


        log.info("Sending message to SQS");
//        queueMessagingTemplate.convertAndSend(QUEUE_NAME, PAYLOAD, headers);
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, payload);
        log.info("Message sent! -- {}", payload);

    }

}
