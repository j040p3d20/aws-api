package org.learn.aws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/meetings")
public class MeetingsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Meeting> list() {
        return List.of(
            Meeting.builder()
                  .agentId("agent 1")
                  .build(),
            Meeting.builder()
                  .agentId("agent 2")
                    .build()
       );
    }
}