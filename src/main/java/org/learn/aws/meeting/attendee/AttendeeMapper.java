package org.learn.aws.meeting.attendee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.amazon.awssdk.services.chimesdkmeetings.model.Attendee;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface AttendeeMapper {

    @Mapping(target = "externalUserId", expression = "java(attendee.externalUserId())")
    @Mapping(target = "attendeeId", expression = "java(attendee.attendeeId())")
    @Mapping(target = "joinToken", expression = "java(attendee.joinToken())")
    AttendeeDto map(Attendee attendee);

    List<AttendeeDto> map(List<Attendee> attendees);

}
