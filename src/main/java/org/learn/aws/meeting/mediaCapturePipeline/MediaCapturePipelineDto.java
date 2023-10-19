package org.learn.aws.meeting.mediaCapturePipeline;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class MediaCapturePipelineDto {
    private final String mediaPipelineId;
    private final String mediaPipelineArn;
    private final String sourceType;
    private final String sourceArn;
    private final String status;
    private final String sinkType;
    private final String sinkArn;
    private final Instant createdTimestamp;
    private final Instant updatedTimestamp;
}
