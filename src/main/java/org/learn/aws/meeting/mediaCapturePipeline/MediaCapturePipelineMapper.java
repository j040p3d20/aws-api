package org.learn.aws.meeting.mediaCapturePipeline;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.MediaCapturePipeline;

@Mapper(componentModel = "cdi")
public interface MediaCapturePipelineMapper {

    @Mapping(target = "mediaPipelineId", expression = "java(mediaCapturePipeline.mediaPipelineId())")
    @Mapping(target = "mediaPipelineArn", expression = "java(mediaCapturePipeline.mediaPipelineArn())")
    @Mapping(target = "sourceType", expression = "java(mediaCapturePipeline.sourceTypeAsString())")
    @Mapping(target = "sourceArn", expression = "java(mediaCapturePipeline.sourceArn())")
    @Mapping(target = "status", expression = "java(mediaCapturePipeline.statusAsString())")
    @Mapping(target = "sinkType", expression = "java(mediaCapturePipeline.sinkTypeAsString())")
    @Mapping(target = "sinkArn", expression = "java(mediaCapturePipeline.sinkArn())")
    @Mapping(target = "createdTimestamp", expression = "java(mediaCapturePipeline.createdTimestamp())")
    @Mapping(target = "updatedTimestamp", expression = "java(mediaCapturePipeline.updatedTimestamp())")
    MediaCapturePipelineDto map(MediaCapturePipeline mediaCapturePipeline);

}
