package com.liveklass.liveklass.kafka.consumer;

import com.liveklass.liveklass.dto.EventRequest;
import com.liveklass.liveklass.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final EventService eventService;

    @KafkaListener(
            topics = "${custom.kafka.topic}",
            groupId = "${custom.kafka.consumer-group}"
    )
    public void consume(EventRequest request) {

        log.info("Consumed Event = {}", request);

        eventService.processEvent(request);
    }
}