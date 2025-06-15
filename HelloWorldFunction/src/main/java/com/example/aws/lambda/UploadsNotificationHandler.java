package com.example.aws.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.Context;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;


public class UploadsNotificationHandler implements RequestHandler<SQSEvent, String> {

    private final SnsClient snsClient = SnsClient.create();
    private final String topicArn = "arn:aws:sns:us-east-1:260212807096:sohit-aws-UploadsNotificationTopic";

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        context.getLogger().log("Received " + event.getRecords().size() + " SQS messages");

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String messageBody = msg.getBody();
            context.getLogger().log("Publishing to SNS: " + messageBody);

            snsClient.publish(PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(messageBody)
                    .build());
        }
        return "Done";
    }
}
