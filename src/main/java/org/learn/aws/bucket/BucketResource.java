package org.learn.aws.bucket;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.awscore.presigner.PresignedRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest;
import software.amazon.awssdk.services.s3.model.GetBucketLocationResponse;
import software.amazon.awssdk.services.s3.model.GetBucketWebsiteRequest;
import software.amazon.awssdk.services.s3.model.GetBucketWebsiteResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.util.List;

@Slf4j
@Path("/buckets")
public class BucketResource {

    @Inject
    BucketMapper bucketMapper;

    @Inject
    S3Client s3Client;

    @Inject
    S3Presigner s3Presigner;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BucketDto> list() {
        final ListBucketsResponse response = s3Client.listBuckets();

        response.buckets().forEach(bucket -> {

            final GetBucketWebsiteResponse website =
                null; // s3Client.getBucketWebsite(GetBucketWebsiteRequest.builder().bucket(bucket.name()).build());

            final GetBucketLocationResponse location =
                null; // s3Client.getBucketLocation(GetBucketLocationRequest.builder().bucket(bucket.name()).build());

            log.info("- - - listing contents for bucket : {} location : {} website : {}",
                     bucket, location, website);
            final ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                                                                            .bucket(bucket.name())
                                                                            .build();

            final ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);

            listObjectsResponse.contents().forEach(s3Object -> {
//                GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket.name()).key(s3Object.key()).build();
//                GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(10)).getObjectRequest(getObjectRequest).build();
//                PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
//                log.info("object : {} url : {}", s3Object.key(), presignedGetObjectRequest.url());
                log.info("object : {} url : {}", s3Object);
            });
        });

        log.info(response.buckets().toString());
        return bucketMapper.map(response.buckets());
    }
}