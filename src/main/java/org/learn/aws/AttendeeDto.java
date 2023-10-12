package org.learn.aws;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendeeDto {
    private final String externalUserId;
    private final String attendeeId;
    private final String joinToken;
}
