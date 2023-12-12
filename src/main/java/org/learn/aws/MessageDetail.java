package org.learn.aws;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageDetail {
    private Integer version;
    private String eventType;
    private Instant timestamp;
    private String meetingId;
    private String externalMeetingId;
    private String mediaRegion;
    private String attendeeId;
    private String externalUserId;
    private String networkType;
    private String mediaPipelineId;
}
