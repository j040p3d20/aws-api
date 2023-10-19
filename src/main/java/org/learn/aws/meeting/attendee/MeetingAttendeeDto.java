package org.learn.aws.meeting.attendee;

import lombok.Builder;
import lombok.Data;
import org.learn.aws.meeting.MeetingDto;

@Data
@Builder
public class MeetingAttendeeDto {
    private final MeetingDto meeting;
    private final AttendeeDto attendee;
}
