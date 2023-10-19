package org.learn.aws;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.Cancellable;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/queues")
@Slf4j
public class QueueResource {

    private static final Integer MAX_NUMBEROF_MESSAGES = 10;

    private static final Integer WAIT_TIME_SECONDS = 10;

    @Inject
    SqsClient sqsClient;

    Map<String, Cancellable> subscriptions = new HashMap<>();

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object event) {
        log.info("Destroyed : {}", event);
        subscriptions.forEach((url, cancellable) -> {
            log.info("canceling subscription for {}", url);
            try {
                cancellable.cancel();
                log.info("subscription successfully canceled for {}", url);
            } catch (Exception e) {
                log.info("subscription failed to cancel for " + url, e);
            }
        });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> list() {
        final List<String> urls = sqsClient.listQueues().queueUrls();
        urls.forEach(this::subscribe);
        return urls;
    }

    public List<Message> fetchMessages(Long l, String url) {
        log.info("fetching messages for tick {} : queue : {}", l, url);
        final ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                                                                   .queueUrl(url)
                                                                   .maxNumberOfMessages(MAX_NUMBEROF_MESSAGES)
                                                                   .waitTimeSeconds(WAIT_TIME_SECONDS)
                                                                   .visibilityTimeout(3600)
                                                                   .build();
        final ReceiveMessageResponse response = sqsClient.receiveMessage(request);
        log.info("received messages for queue : {} : {}", url, response.messages().size());
        return response.messages();
    }

    @Synchronized private void subscribe(String url) {

//        https://github.com/smallrye/smallrye-reactive-messaging/issues/1117

//        public PublisherBuilder<? extends Message<?>> getSource() {
//            Multi<Message<?>> publisher = Uni.createFrom()
//                                             .completionStage(() -> sqsClient.receiveMessage(m -> m.queueUrl(url)))
//                                             .repeat().indefinitely()
//                                             .invoke(() -> System.out.println("Log SQS invocation"))
//                                             .onItem().transformToIterable(ReceiveMessageResponse::messages)
//                                             .onItem().transform(Message::of);
//
//            return ReactiveStreams.fromPublisher(publisher);
//        }

//        https://smallrye.io/smallrye-mutiny/2.0.0/guides/delaying-events/#throttling-a-multi

//        https://smallrye.io/smallrye-mutiny/2.5.1/guides/polling/


        if (!subscriptions.containsKey(url)) {
            log.info("creating subscription for {}", url);
            Cancellable cancellable =
                Uni.createFrom().item(() -> fetchMessages(0L, url))
                   .repeat().indefinitely()
                   .emitOn(Infrastructure.getDefaultWorkerPool())
                   .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                   .flatMap(Multi.createFrom()::iterable)
                   .invoke(m -> {
                       sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(url).receiptHandle(m.receiptHandle()).build());
                       log.info("queue {} message {} processed and deleted", url, m.body());
                   })
                   .subscribe().with(
                       m -> log.debug("queue {} message {}", url, m),
                       ex -> log.error("queue " + url + " error", ex),
                       () -> log.info("queue {} completed", url));
            subscriptions.put(url, cancellable);
        } else {
            log.info("subscription already exists for {}", url);
        }
    }
}