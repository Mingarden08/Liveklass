package com.liveklass.liveklass.generator;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.dto.EventRequest;
import com.liveklass.liveklass.kafka.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class EventGenerator {

    private final EventProducer eventProducer;

    private final Random random = new Random();

    private final String[] pageUrls = {"main", "search", "login", "add", "error"};
    private final int[] pageUrlWeights = {40, 23, 20, 14, 3};
    private final String[] searchKeywords = {"keyboard", "mouse", "monitor", "headphone", "charger", "laptop"};
    private final String[] errorMessages = {"null pointer exception", "timeout error", "network error", "invalid request", "server error"};
    
        // Weighted event type selection: PAGE_VIEWED, PRODUCT_VIEWED, SEARCH_EXECUTED, CART_ITEM_ADDED, ERROR_OCCURRED
        private final EventType[] weightedEventTypes = {
            EventType.PAGE_VIEWED,
            EventType.PRODUCT_VIEWED,
            EventType.SEARCH_EXECUTED,
            EventType.CART_ITEM_ADDED,
            EventType.ERROR_OCCURRED
        };

        private final int[] weights = {35, 25, 20, 15, 5}; // sums to 100
        private final int totalWeight = 100;

    @Scheduled(fixedRate = 100)
    public void generateEvent() {

        EventType eventType = pickWeightedEventType();

        String searchKeyword = "";
        String pageUrl = "";
        String errorMessage = "";
        String productId = "0";  // 문자열로 변경, 초기값 "0"

        // 이벤트 타입별 조건부 데이터 생성
        switch (eventType) {
            case SEARCH_EXECUTED -> {
                searchKeyword = searchKeywords[random.nextInt(searchKeywords.length)];
                productId = String.format("p_%03d", random.nextInt(50) + 1);
            }
            case PRODUCT_VIEWED -> {
                productId = String.format("p_%03d", random.nextInt(50) + 1);
                pageUrl = "/products/" + productId;
            }
            case CART_ITEM_ADDED -> productId = String.format("p_%03d", random.nextInt(50) + 1);
            case PAGE_VIEWED -> pageUrl = pickWeightedPageUrl();
            case ERROR_OCCURRED -> errorMessage = errorMessages[random.nextInt(errorMessages.length)];
        }

        // userId: 1~200 범위, "u_001" 형식
        String userId = String.format("u_%03d", random.nextInt(100) + 1);

        EventRequest event = new EventRequest();
        event.setEventType(eventType);
        event.setUserId(userId);
        event.setProductId(productId);
        event.setSearchKeyword(searchKeyword);
        event.setPageUrl(pageUrl);
        event.setErrorMessage(errorMessage);

        eventProducer.send(event);
    }

    private EventType pickWeightedEventType() {
        int r = random.nextInt(totalWeight);
        int cum = 0;
        for (int i = 0; i < weights.length; i++) {
            cum += weights[i];
            if (r < cum) {
                return weightedEventTypes[i];
            }
        }
        return weightedEventTypes[weightedEventTypes.length - 1];
    }

    private String pickWeightedPageUrl() {
        int totalPageUrlWeight = 0;
        for (int weight : pageUrlWeights) {
            totalPageUrlWeight += weight;
        }

        int r = random.nextInt(totalPageUrlWeight);
        int cum = 0;
        for (int i = 0; i < pageUrlWeights.length; i++) {
            cum += pageUrlWeights[i];
            if (r < cum) {
                return pageUrls[i];
            }
        }

        return pageUrls[pageUrls.length - 1];
    }
}