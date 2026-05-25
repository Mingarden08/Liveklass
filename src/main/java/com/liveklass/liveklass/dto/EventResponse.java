package com.liveklass.liveklass.dto;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.domain.EventLog;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventResponse {
    private final Long id;
    private final EventType eventType;
    private final String userId;
    private final String productId;
    private final String searchKeyword;
    private final String pageUrl;
    private final String errorMessage;
    private final LocalDateTime createdAt;

    public static EventResponse from(EventLog eventLog) {
        return EventResponse.builder()
                .id(eventLog.getId())
                .eventType(eventLog.getEventType())
                .userId(eventLog.getUserId())
                .productId(eventLog.getProductId())
                .searchKeyword(eventLog.getSearchKeyword())
                .pageUrl(eventLog.getPageUrl())
                .errorMessage(eventLog.getErrorMessage())
                .createdAt(eventLog.getCreatedAt())
                .build();
    }
}