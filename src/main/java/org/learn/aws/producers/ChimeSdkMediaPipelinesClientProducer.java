package org.learn.aws.producers;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.chimesdkmediapipelines.ChimeSdkMediaPipelinesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class ChimeSdkMediaPipelinesClientProducer {

    @Inject
    AwsCredentialsProvider awsCredentialsProvider;

    @Produces
    public ChimeSdkMediaPipelinesClient chimeSdkMediaPipelinesClient() {
        return ChimeSdkMediaPipelinesClient.builder()
                                           .credentialsProvider(awsCredentialsProvider)
                                           .build();
    }
}
