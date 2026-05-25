package com.liveklass.liveklass.generator;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.dto.EventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
public class EventGenerator {

    private final Random random = new Random();

    private static final List<String> PAGE_URLS = List.of(
            "/home", "/products", "/cart",
            "/mypage", "/search", "/checkout"
    );
    private static final List<String> SEARCH_KEYWORDS = List.of(
            "자바 강의", "스프링 부트", "카프카 입문",
            "도커 튜토리얼", "파이썬 기초", "데이터베이스"
    );
    private static final List<String> ERROR_MESSAGES = List.of(
            "NullPointerException in OrderService",
            "Connection timeout to payment gateway",
            "Failed to load product image",
            "JWT token expired"
    );

    @Scheduled(fixedDelay = 1000)
    public void generate() {
        EventRequest event = buildRandomEvent();
        log.info("[EventGenerator] type={} userId={} sessionId={}",
                event.getEventType(), event.getUserId(), event.getSessionId());
        // TODO: Kafka Producer 완성 후 교체
        // kafkaProducer.send(event);
    }

    private EventRequest buildRandomEvent() {
        EventType type = randomEventType();
        return switch (type) {
            case PAGE_VIEWED     -> pageViewed();
            case PRODUCT_VIEWED  -> productViewed(); // 오타 수정
            case CART_ITEM_ADDED -> cartItemAdded();
            case SEARCH_EXECUTED -> searchExecuted();
            case ERROR_OCCURRED  -> errorOccurred();
        };
    }

    // ── 이벤트별 생성 메서드 ──────────────────────────

    private EventRequest pageViewed() {
        return EventRequest.builder()
                .eventType(EventType.PAGE_VIEWED)
                .userId(randomUserId())
                .sessionId(randomSessionId())
                .pageUrl(randomFrom(PAGE_URLS))
                .build();
    }

    private EventRequest productViewed() { // 오타 수정: productViewd → productViewed
        return EventRequest.builder()
                .eventType(EventType.PRODUCT_VIEWED)
                .userId(randomUserId())
                .sessionId(randomSessionId())
                .productId(randomProductId())
                .pageUrl("/products/" + randomProductId())
                .build();
    }

    private EventRequest cartItemAdded() {
        return EventRequest.builder()
                .eventType(EventType.CART_ITEM_ADDED)
                .userId((long)(random.nextInt(900) + 100)) // 로그인 필수
                .sessionId(randomSessionId())
                .productId(randomProductId())
                .build();
    }

    private EventRequest searchExecuted() {
        return EventRequest.builder()
                .eventType(EventType.SEARCH_EXECUTED)
                .userId(randomUserId())
                .sessionId(randomSessionId())
                .searchKeyword(randomFrom(SEARCH_KEYWORDS))
                .pageUrl("/search")
                .build();
    }

    private EventRequest errorOccurred() {
        return EventRequest.builder()
                .eventType(EventType.ERROR_OCCURRED)
                .userId(randomUserId())
                .sessionId(randomSessionId())
                .errorMessage(randomFrom(ERROR_MESSAGES))
                .build();
    }

    // ── 랜덤 유틸 ────────────────────────────────────

    private EventType randomEventType() {
        EventType[] types = EventType.values();
        return types[random.nextInt(types.length)];
    }

    private Long randomUserId() {
        return random.nextBoolean()
                ? (long)(random.nextInt(900) + 100)
                : null;  // 비로그인 50%
    }

    private String randomSessionId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private Long randomProductId() {
        return (long)(random.nextInt(50) + 1);
    }

    private <T> T randomFrom(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
