package org.learn.aws;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingAttendeeDto {
    private final MeetingDto meeting;
    private final AttendeeDto attendee;
}
