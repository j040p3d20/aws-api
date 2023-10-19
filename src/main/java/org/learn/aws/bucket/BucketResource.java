package org.learn.aws.bucket;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@Path("/buckets")
public class BucketResource {

    @Inject
    BucketMapper bucketMapper;

    @Inject
    S3Client sClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BucketDto> list() {
        final ListBucketsResponse response = sClient.listBuckets();
        log.info(response.buckets().toString());
        return bucketMapper.map(response.buckets());
    }
}