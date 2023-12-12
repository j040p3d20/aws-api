package org.learn.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@Slf4j
@QuarkusTest
public class MessageTest {

    private String MEETING = "{\"version\":\"0\",\"source\":\"aws.chime\",\"account\":\"622938434879\",\"region\":\"us-east-1\",\"detail-type\":\"Chime Meeting State Change\",\"time\":\"2023-11-08T12:11:16.120Z\",\"detail\":{\"version\":\"0\",\"eventType\":\"chime:AttendeeLeft\",\"timestamp\":1699445476120,\"meetingId\":\"93b144a0-804e-4666-ae9b-153807d22713\",\"attendeeId\":\"9fcbe2ca-173b-21fb-207b-58ca7d30962f\",\"externalUserId\":\"aws:MediaPipeline-4ae1f\",\"networkType\":\"Voip\",\"externalMeetingId\":\"29bb81ec-cc50-44ef-9453-706a7b49862d\",\"mediaRegion\":\"us-east-1\"}}";
    private String PIPELINE = "{\"version\":\"0\",\"source\":\"aws.chime\",\"account\":\"622938434879\",\"region\":\"us-east-1\",\"detail-type\":\"Chime Media Pipeline State Change\",\"time\":\"2023-11-08T12:11:51.666Z\",\"detail\":{\"version\":\"0\",\"eventType\":\"chime:MediaPipelineDeleted\",\"timestamp\":1699445511666,\"meetingId\":\"93b144a0-804e-4666-ae9b-153807d22713\",\"externalMeetingId\":\"29bb81ec-cc50-44ef-9453-706a7b49862d\",\"mediaPipelineId\":\"c411477c-9c05-430f-ae5b-393a7f59e3b5\",\"mediaRegion\":\"us-east-1\"}}";
    private String BASE = "{\"version\":\"0\"}";

    @Inject
    ObjectMapper mapper;

    @Test
    public void testMessage() throws Exception {

        Message message = mapper.readValue(BASE, Message.class);
        log.info("message: {}", message);

        message = mapper.readValue(MEETING, Message.class);
        log.info("message: {}", message);

        message = mapper.readValue(PIPELINE, Message.class);
        log.info("message: {}", message);
    }
}
