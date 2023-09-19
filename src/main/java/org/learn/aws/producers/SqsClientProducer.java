package org.learn.aws.producers;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class SqsClientProducer {

    @Inject
    AwsCredentialsProvider awsCredentialsProvider;

    @Produces
    public SqsClient sqsClient() {
        return SqsClient.builder().credentialsProvider(awsCredentialsProvider).build();
    }
}
