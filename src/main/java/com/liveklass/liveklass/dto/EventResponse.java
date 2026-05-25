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
    private Long userId;
    private String sessionId;
    private LocalDateTime createdAt;

    public static EventResponse from(EventLog eventLog) {
        return EventResponse.builder()
                .id(eventLog.getId())
                .eventType(eventLog.getEventType())
                .userId(eventLog.getUserId())
                .sessionId(eventLog.getSessionId())
                .createdAt(eventLog.getCreatedAt())
                .build();
    }
}
