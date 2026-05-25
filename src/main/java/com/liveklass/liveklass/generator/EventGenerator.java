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

    private final String[] pageUrls = {"main", "login", "shop", "payment", "error"};
    private final String[] searchKeywords = {"keyboard", "mouse", "monitor", "headphone", "charger", "laptop"};
    private final String[] errorMessages = {"null pointer exception", "timeout error", "network error", "invalid request", "server error"};

    @Scheduled(fixedRate = 1000)
    public void generateEvent() {

        EventType[] eventTypes = EventType.values();
        EventType eventType = eventTypes[random.nextInt(eventTypes.length)];

        String searchKeyword = "";
        String pageUrl = "";
        String errorMessage = "";
        String productId = "0";  // 문자열로 변경, 초기값 "0"

        // 이벤트 타입별 조건부 데이터 생성
        if (eventType == EventType.SEARCH_EXECUTED) {
            searchKeyword = searchKeywords[random.nextInt(searchKeywords.length)];
            productId = String.format("p_%03d", random.nextInt(50) + 1);
        } else if (eventType == EventType.PRODUCT_VIEWED) {
            productId = String.format("p_%03d", random.nextInt(50) + 1);
            pageUrl = "/products/" + productId;
        } else if (eventType == EventType.CART_ITEM_ADDED) {
            productId = String.format("p_%03d", random.nextInt(50) + 1);
        } else if (eventType == EventType.PAGE_VIEWED) {
            pageUrl = pageUrls[random.nextInt(pageUrls.length)];
        } else if (eventType == EventType.ERROR_OCCURRED) {
            errorMessage = errorMessages[random.nextInt(errorMessages.length)];
        }

        // userId: 1~200 범위, "u_001" 형식
        String userId = String.format("u_%03d", random.nextInt(100) + 1);

        EventRequest event = EventRequest.builder()
                .eventType(eventType)
                .userId(userId)                    // String으로 전달
                .sessionId(UUID.randomUUID().toString())
                .productId(productId)              // String으로 전달 (포맷팅됨)
                .searchKeyword(searchKeyword)
                .pageUrl(pageUrl)
                .errorMessage(errorMessage)
                .build();

        eventProducer.send(event);
    }
}