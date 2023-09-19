package org.learn.aws;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/queues") public class QueuesResource {

    @Inject
    QueueMapper queueMapper;

    @Inject
    SqsClient sqsClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List list() {
        final ListQueuesResponse response = sqsClient.listQueues();
        sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder().queueUrl("").build());
        return response.queueUrls();
    }
}