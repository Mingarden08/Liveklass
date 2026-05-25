package com.liveklass.liveklass.kafka.producer;

import com.liveklass.liveklass.dto.EventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventKafkaProducer {

    private final KafkaTemplate<String, EventRequest> kafkaTemplate;

    @Value("${custom.kafka.topic}")
    private String topic;

    public void send(EventRequest event) {
        CompletableFuture<SendResult<String, EventRequest>> future =
                kafkaTemplate.send(topic, event.getEventType().name(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[Producer] 전송 실패 | type={} error={}",
                        event.getEventType(), ex.getMessage());
                return;
            }
            log.debug("[Producer] 전송 성공 | type={} offset={}",
                    event.getEventType(),
                    result.getRecordMetadata().offset());
        });
    }
}
