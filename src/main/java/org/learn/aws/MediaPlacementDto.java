package org.learn.aws;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaPlacementDto {
    private final String audioHostUrl;
    private final String audioFallbackUrl;
    private final String signalingUrl;
    private final String turnControlUrl;
    private final String screenDataUrl;
    private final String screenViewingUrl;
    private final String screenSharingUrl;
    private final String eventIngestionUrl;
}

