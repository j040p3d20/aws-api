package org.learn.aws;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BucketDto {
    private final String name;
    private final Instant creationDate;
}
