package org.learn.aws;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class BucketDto {
    private final String name;
    private final Instant creationDate;
}
