package org.learn.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.Instant;

@Data
public class Message {
    private Integer version;
    private String source;
    private String account;
    private String region;
    private Instant time;
    @JsonProperty("detail-type")
    private String detailType;
    private MessageDetail detail;
}
