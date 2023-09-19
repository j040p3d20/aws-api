package org.learn.aws.producers;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class S3ClientProducer {

    @Inject
    AwsCredentialsProvider awsCredentialsProvider;

    @Produces
    public S3Client s3Client() {
        return S3Client.builder().credentialsProvider(awsCredentialsProvider).build();
    }
}
