package com.liveklass.liveklass.dto;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.domain.EventLog;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventResponse {
    private Long id;
    private EventType eventType;
    private String userId;        // Long → String으로 변경
    private String sessionId;
    private String productId;     // Long → String으로 변경
    private String searchKeyword;
    private String pageUrl;
    private String errorMessage;
    private LocalDateTime createdAt;

    public static EventResponse from(EventLog eventLog) {
        return EventResponse.builder()
                .id(eventLog.getId())
                .eventType(eventLog.getEventType())
                .userId(String.format("U_%03d", eventLog.getUserId()))
                .sessionId(eventLog.getSessionId())
                .productId(String.format("P_%03d", eventLog.getProductId()))
                .searchKeyword(eventLog.getSearchKeyword())
                .pageUrl(eventLog.getPageUrl())
                .errorMessage(eventLog.getErrorMessage())
                .createdAt(eventLog.getCreatedAt())
                .build();
    }
}