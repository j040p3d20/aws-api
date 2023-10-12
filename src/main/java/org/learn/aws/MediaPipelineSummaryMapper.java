package org.learn.aws;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import software.amazon.awssdk.services.chimesdkmediapipelines.ChimeSdkMediaPipelinesClient;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.GetMediaPipelineRequest;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.GetMediaPipelineResponse;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.MediaPipeline;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.MediaPipelineSummary;

import javax.inject.Inject;
import java.util.List;

@Mapper(componentModel = "cdi")
public abstract class MediaPipelineSummaryMapper {

    @Inject
    ChimeSdkMediaPipelinesClient chimeSdkMediaPipelinesClient;

    @Mapping(target = "id", expression = "java(summary.mediaPipelineId())")
    @Mapping(target = "arn", expression = "java(summary.mediaPipelineArn())")
    @Mapping(target = "type", source = "summary", qualifiedByName = "mapType")
    public abstract MediaPipelineSummaryDto map(MediaPipelineSummary summary);

    public abstract List<MediaPipelineSummaryDto> map(List<MediaPipelineSummary> mediaPipelineSummaries);

    @Named("mapType")
    public String mapType(MediaPipelineSummary mediaPipelineSummary) {

        final GetMediaPipelineRequest request =
            GetMediaPipelineRequest.builder()
                                   .mediaPipelineId(mediaPipelineSummary.mediaPipelineId())
                                   .build();

        final GetMediaPipelineResponse response = chimeSdkMediaPipelinesClient.getMediaPipeline(request);
        final MediaPipeline mediaPipeline = response.mediaPipeline();

        if (mediaPipeline.mediaLiveConnectorPipeline() != null) {
            return "mediaLiveConnectorPipeline";
        }
        if (mediaPipeline.mediaConcatenationPipeline() != null) {
            return "mediaConcatenationPipeline";
        }
        if (mediaPipeline.mediaInsightsPipeline() != null) {
            return "mediaInsightsPipeline";
        }
        if (mediaPipeline.mediaCapturePipeline() != null) {
            return "mediaCapturePipeline";
        }
        return null;

    }

}
