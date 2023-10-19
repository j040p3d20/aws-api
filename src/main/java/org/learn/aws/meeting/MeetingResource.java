package org.learn.aws.meeting;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateMeetingRequest;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateMeetingResponse;
import software.amazon.awssdk.services.chimesdkmeetings.model.NotificationsConfiguration;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/meetings") public class MeetingResource {

    public static final String MEETINGS_KEY = "meetings";

    @Inject
    ReactiveRedisDataSource ds;

    @Inject
    ChimeSdkMeetingsClient chimeSdkMeetingsClient;

    @Inject
    MeetingMapper meetingMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<MeetingDto>> list() {
//        CreateAttendeeRequestItem createAttendeeRequest =
//            CreateAttendeeRequestItem.builder()
//                                     .externalUserId("externalUserId")
//                                     .build();
        return ds.list(MeetingDto.class)
//                 .sort(MEETINGS_KEY, new SortArgs().alpha())
                 .lrange(MEETINGS_KEY, 0, -1).map(list -> list);
    }

    @POST
    public Uni<MeetingDto> create() {

        NotificationsConfiguration notificationsConfiguration =
            NotificationsConfiguration.builder().sqsQueueArn("arn:aws:sqs:us-east-1:622938434879:q").build();

        CreateMeetingRequest request = CreateMeetingRequest.builder()
                                                           .mediaRegion("us-east-1")
                                                           .notificationsConfiguration(notificationsConfiguration)
                                                           .externalMeetingId(UUID.randomUUID().toString())
                                                           .build();

        CreateMeetingResponse response = chimeSdkMeetingsClient.createMeeting(request);
        final MeetingDto meetingDto = meetingMapper.map(response.meeting());
        return ds.list(MeetingDto.class).lpush(MEETINGS_KEY, meetingDto).map(len -> meetingDto);
    }

}
