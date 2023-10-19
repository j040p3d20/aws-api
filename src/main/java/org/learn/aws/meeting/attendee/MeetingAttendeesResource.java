package org.learn.aws.meeting.attendee;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Uni;
import org.learn.aws.meeting.MeetingDto;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateAttendeeRequest;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateAttendeeResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.UUID;

import static org.learn.aws.meeting.MeetingResource.MEETINGS_KEY;

@Path("/meetings/{meetingId}/attendees")
public class MeetingAttendeesResource {

    @Inject
    ReactiveRedisDataSource ds;

    @Inject
    ChimeSdkMeetingsClient chimeSdkMeetingsClient;

    @Inject
    AttendeeMapper attendeeMapper;

    @GET
    public Uni<List<MeetingAttendeeDto>> list(@PathParam("meetingId") String meetingId) {
        return ds.list(MeetingAttendeeDto.class)
                 .lrange(meetingId, 0, -1)
//                 .sort(MEETINGS_KEY, new SortArgs().alpha())
                 .map(list -> list)
            ;
    }

    @POST
    public Uni<MeetingAttendeeDto> create(@PathParam("meetingId") String meetingId) {

        CreateAttendeeRequest createAttendeeRequest =
            CreateAttendeeRequest.builder().meetingId(meetingId).externalUserId(UUID.randomUUID().toString()).build();

        CreateAttendeeResponse createAttendeeResponse = chimeSdkMeetingsClient.createAttendee(createAttendeeRequest);
        AttendeeDto attendeeDto = attendeeMapper.map(createAttendeeResponse.attendee());


        return ds.list(MeetingDto.class)
                 .lrange(MEETINGS_KEY, 0, -1)
                 .map(meetingDtos -> meetingDtos.stream()
                                                .filter(meetingDto -> meetingDto.getMeetingId().equals(meetingId))
                                                .findFirst()
                                                .orElseThrow(NotFoundException::new))
                 .map(meetingDto -> MeetingAttendeeDto.builder().meeting(meetingDto).attendee(attendeeDto).build())
                 .flatMap(meetingAttendeeDto -> ds.list(MeetingAttendeeDto.class)
                                                  .lpush(meetingId, meetingAttendeeDto)
                                                  .map(len -> meetingAttendeeDto));
    }

}
