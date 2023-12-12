package org.learn.aws.producers;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class S3PresignerProducer {

    @Inject
    AwsCredentialsProvider awsCredentialsProvider;

    @Produces
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                          .credentialsProvider(awsCredentialsProvider)
                          .build();
    }

}
