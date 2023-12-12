package org.learn.aws;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.time.Duration;

@Slf4j
@ApplicationScoped
public class EventBean {


    public void onStart(@Observes final StartupEvent startupEvent) {
        log.info("EventBean.onStart : {}", startupEvent);

//        Multi.createFrom().ticks().every(Duration.ofSeconds(2))
//            .onItem().invoke( n -> {
//                log.info("EventBean.onStart : {}", n);
//                if ( n%5 == 4) throw new RuntimeException("OhNo : " + n);
//            })
//            .subscribe().with(
//                item -> log.info("onItem : {}", item),
//                failure -> log.error("onFailure : {}", failure),
//                () -> log.info("onCompleted")
//            );
    }
}
