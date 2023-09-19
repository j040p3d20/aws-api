package org.learn.aws;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/buckets") public class BucketsResource {

    @Inject
    BucketMapper bucketMapper;

    @Inject
    S3Client sClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BucketDto> list() {
        final ListBucketsResponse list = sClient.listBuckets();
        return bucketMapper.map(list.buckets());
    }
}