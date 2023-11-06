package org.learn.aws.producers;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class AwsCredentialsProviderProducer {

    @Produces
    public AwsCredentialsProvider awsCredentialsProvider() {
        return ProfileCredentialsProvider.builder()
                                         .profileName("chime-service-user")
                                         .build();
    }
}
