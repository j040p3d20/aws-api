package org.learn.aws.meeting.mediaCapturePipeline;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.learn.aws.meeting.MeetingDto;
import software.amazon.awssdk.services.chimesdkmediapipelines.ChimeSdkMediaPipelinesClient;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ArtifactsConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ArtifactsConcatenationState;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.AudioArtifactsConcatenationState;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.AudioConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ChimeSdkMeetingConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.CompositedVideoConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ConcatenationSink;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ConcatenationSource;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ConcatenationSourceType;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ContentConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.CreateMediaCapturePipelineRequest;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.CreateMediaCapturePipelineResponse;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.CreateMediaConcatenationPipelineRequest;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.CreateMediaConcatenationPipelineResponse;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.DataChannelConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.DeleteMediaCapturePipelineRequest;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.MediaCapturePipelineSourceConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.MeetingEventsConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.S3BucketSinkConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.TranscriptionMessagesConcatenationConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.VideoConcatenationConfiguration;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.util.List;

import static org.learn.aws.meeting.MeetingResource.MEETINGS_KEY;

@Slf4j
@Path("/meetings/{meetingId}/mediaCapturePipelines")
public class MediaCapturePipelineResource {

    @Inject
    ChimeSdkMediaPipelinesClient chimeSdkMediaPipelinesClient;

    @Inject
    MediaCapturePipelineMapper mapper;

    @Inject
    ReactiveRedisDataSource ds;

    @DELETE
    @Path("/{mediaPipelineId}")
    public void stopMediaCapturePipeline(
        @PathParam("meetingId") String meetingId,
        @PathParam("mediaPipelineId") String mediaPipelineId) {
        delete(mediaPipelineId);
        log.info("media capture pipeline stopped for meeting {} : {}", meetingId, mediaPipelineId);
    }

    private void delete(String mediaPipelineId) {
        final DeleteMediaCapturePipelineRequest request =
            DeleteMediaCapturePipelineRequest.builder()
                                             .mediaPipelineId(mediaPipelineId)
                                             .build();
        chimeSdkMediaPipelinesClient.deleteMediaCapturePipeline(request);
    }

    @POST
    public Uni<MediaCapturePipelineDto> startMediaCapturePipeline(@PathParam("meetingId") String meetingId) {
        log.info("start media capture pipeline for meeting {}", meetingId);
        return find(meetingId).map(this::create);
    }

    private Uni<MeetingDto> find(String meetingId) {
        return ds.list(MeetingDto.class)
                 .lrange(MEETINGS_KEY, 0, -1)
                 .map(meetingDtos -> meetingDtos.stream()
                                                .filter(meetingDto -> meetingDto.getMeetingId().equals(meetingId))
                                                .findFirst()
                                                .orElseThrow(NotFoundException::new));
    }

    // Create a service-linked role to allow Amazon Chime SDK media pipelines
    // to access Amazon Chime SDK meetings on your behalf
    // https://docs.aws.amazon.com/chime-sdk/latest/dg/create-pipeline-role.html

    private MediaCapturePipelineDto create(MeetingDto meeting) {

        // create media capture pipeline

        final CreateMediaCapturePipelineRequest captureRequest =
            CreateMediaCapturePipelineRequest.builder()
                                             .sourceType("ChimeSdkMeeting")
                                             .sourceArn(meeting.getMeetingArn())
                                             .sinkType("S3Bucket")
                                             .sinkArn("arn:aws:s3:::org.learn.aws.chime")
                                             .build();

        final CreateMediaCapturePipelineResponse captureResponse =
            chimeSdkMediaPipelinesClient.createMediaCapturePipeline(captureRequest);

        log.info("CreateMediaCapturePipelineResponse : {}", captureResponse);

        // create media concatenation pipeline

        final String mediaPipelineArn = captureResponse.mediaCapturePipeline().mediaPipelineArn();
        CreateMediaConcatenationPipelineRequest concatenationRequest =
            CreateMediaConcatenationPipelineRequest.builder()
                                                   .sinks(List.of(concatenationSink()))
                                                   .sources(List.of(concatenationSource(mediaPipelineArn)))
                                                   .build();

        final CreateMediaConcatenationPipelineResponse concatenationResponse =
            chimeSdkMediaPipelinesClient.createMediaConcatenationPipeline(concatenationRequest);

            log.info("CreateMediaConcatenationPipelineResponse : {}", concatenationResponse);

        return mapper.map(captureResponse.mediaCapturePipeline());
    }

    private ConcatenationSink concatenationSink() {
        return ConcatenationSink
            .builder()
            .type("S3Bucket")
            .s3BucketSinkConfiguration(s3BucketSinkConfiguration())
            .build();
    }

    private S3BucketSinkConfiguration s3BucketSinkConfiguration() {
        return S3BucketSinkConfiguration
            .builder()
            .destination("arn:aws:s3:::org.learn.aws.chime")
            .build();
    }

    private ConcatenationSource concatenationSource(String mediaPipelineArn) {
        return ConcatenationSource
            .builder()
            .type(ConcatenationSourceType.MEDIA_CAPTURE_PIPELINE)
            .mediaCapturePipelineSourceConfiguration(mediaCapturePipelineSourceConfiguration(mediaPipelineArn))
            .build();
    }

    private MediaCapturePipelineSourceConfiguration mediaCapturePipelineSourceConfiguration(String mediaPipelineArn) {
        return MediaCapturePipelineSourceConfiguration
            .builder()
            .mediaPipelineArn(mediaPipelineArn)
            .chimeSdkMeetingConfiguration(meetingConfiguration())
            .build();
    }

    private ChimeSdkMeetingConcatenationConfiguration meetingConfiguration() {
        return ChimeSdkMeetingConcatenationConfiguration
            .builder()
            .artifactsConfiguration(artifactsConfiguration())
            .build();
    }

    private ArtifactsConcatenationConfiguration artifactsConfiguration() {
        return ArtifactsConcatenationConfiguration
            .builder()
            .audio(AudioConcatenationConfiguration.builder().state(AudioArtifactsConcatenationState.ENABLED).build())
            .compositedVideo(CompositedVideoConcatenationConfiguration.builder().state(ArtifactsConcatenationState.ENABLED).build())
            .content(ContentConcatenationConfiguration.builder().state(ArtifactsConcatenationState.ENABLED).build())
            .dataChannel(DataChannelConcatenationConfiguration.builder().state(ArtifactsConcatenationState.ENABLED).build())
            .meetingEvents(MeetingEventsConcatenationConfiguration.builder().state(ArtifactsConcatenationState.ENABLED).build())
            .transcriptionMessages(TranscriptionMessagesConcatenationConfiguration.builder().state(ArtifactsConcatenationState.ENABLED).build())
            .video(VideoConcatenationConfiguration.builder().state(ArtifactsConcatenationState.ENABLED).build())
            .build();
    }


}
