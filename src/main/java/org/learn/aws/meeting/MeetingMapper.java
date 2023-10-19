package org.learn.aws.meeting;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.amazon.awssdk.services.chimesdkmeetings.model.MediaPlacement;
import software.amazon.awssdk.services.chimesdkmeetings.model.Meeting;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface MeetingMapper {

    @Mapping(target = "audioHostUrl", expression = "java(mediaPlacement.audioHostUrl())")
    @Mapping(target = "audioFallbackUrl", expression = "java(mediaPlacement.audioFallbackUrl())")
    @Mapping(target = "signalingUrl", expression = "java(mediaPlacement.signalingUrl())")
    @Mapping(target = "turnControlUrl", expression = "java(mediaPlacement.turnControlUrl())")
    @Mapping(target = "screenDataUrl", expression = "java(mediaPlacement.screenDataUrl())")
    @Mapping(target = "screenViewingUrl", expression = "java(mediaPlacement.screenViewingUrl())")
    @Mapping(target = "screenSharingUrl", expression = "java(mediaPlacement.screenSharingUrl())")
    @Mapping(target = "eventIngestionUrl", expression = "java(mediaPlacement.eventIngestionUrl())")
    MediaPlacementDto map(MediaPlacement mediaPlacement);

    @Mapping(target = "meetingId", expression = "java(meeting.meetingId())")
    @Mapping(target = "meetingArn", expression = "java(meeting.meetingArn())")
    @Mapping(target = "externalMeetingId", expression = "java(meeting.externalMeetingId())")
    @Mapping(target = "mediaRegion", expression = "java(meeting.mediaRegion())")
    @Mapping(target = "mediaPlacement", expression = "java(map(meeting.mediaPlacement()))")
    MeetingDto map(Meeting meeting);

    List<MeetingDto> map(List<Meeting> Meetings);

}
