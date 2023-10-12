package org.learn.aws.producers;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class ChimeSdkMeetingsClientProducer {

    @Inject
    AwsCredentialsProvider awsCredentialsProvider;

    @Produces
    public ChimeSdkMeetingsClient chimeSdkMeetingsClient() {
        return ChimeSdkMeetingsClient.builder().credentialsProvider(awsCredentialsProvider).build();
    }
}
