package org.learn.aws.meeting;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingDto {
    private String meetingId;
    private String meetingArn;
    private String externalMeetingId;
    private String mediaRegion;
    private MediaPlacementDto mediaPlacement;
}
