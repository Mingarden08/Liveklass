package com.liveklass.liveklass.generator;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.dto.EventRequest;
import com.liveklass.liveklass.kafka.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventGenerator {

    private final EventProducer eventProducer;

    private final Random random = new Random();

    @Scheduled(fixedRate = 1000)
    public void generateEvent() {

        EventType[] eventTypes = EventType.values();

        EventRequest event = EventRequest.builder()
                .eventType(
                        eventTypes[random.nextInt(eventTypes.length)]
                )
                .userId((long) random.nextInt(100))
                .sessionId(UUID.randomUUID().toString())
                .productId((long) random.nextInt(50))
                .searchKeyword("keyboard")
                .pageUrl("/products/" + random.nextInt(50))
                .errorMessage("sample error")
                .build();

        eventProducer.send(event);
    }
}