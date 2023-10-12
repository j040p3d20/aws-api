package org.learn.aws;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.chimesdkmediapipelines.ChimeSdkMediaPipelinesClient;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.DeleteMediaPipelineRequest;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ListMediaPipelinesRequest;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ListMediaPipelinesResponse;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Slf4j
@Path("/mediaPipelines")
public class MediaPipelineResource {

    @Inject
    MediaPipelineSummaryMapper mapper;

    @Inject
    ChimeSdkMediaPipelinesClient chimeSdkMediaPipelinesClient;

    @GET
    public List<MediaPipelineSummaryDto> list() {

        final ListMediaPipelinesRequest request =
            ListMediaPipelinesRequest.builder()
                                     .maxResults(99)
                                     .build();

        final ListMediaPipelinesResponse response =
            chimeSdkMediaPipelinesClient.listMediaPipelines(request);

        return mapper.map(response.mediaPipelines());
    }

    @DELETE
    @Path("/{mediaPipelineId}")
    public void delete(@PathParam("mediaPipelineId") String mediaPipelineId) {

        final DeleteMediaPipelineRequest request =
            DeleteMediaPipelineRequest.builder()
                                      .mediaPipelineId(mediaPipelineId)
                                      .build();

        chimeSdkMediaPipelinesClient.deleteMediaPipeline(request);

        log.info("Deleted media pipeline {}", mediaPipelineId);
    }
}
