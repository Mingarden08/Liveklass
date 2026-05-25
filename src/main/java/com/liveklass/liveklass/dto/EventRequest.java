package com.liveklass.liveklass.dto;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.domain.EventLog;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
public class EventRequest {

    @NotNull(message = "이벤트 타입은 필수입니다.")
    private EventType eventType;

    private Long userId;
    private String sessionId;
    private Long productId;
    private String searchKeyword;
    private String pageUrl;
    private String errorMessage;

    // Generator 전용 팩토리
    public static EventRequest of(EventType eventType, Long userId,
                                  String sessionId, Long productId, String searchKeyword,
                                  String pageUrl, String errorMessage) {
        EventRequest req = new EventRequest();
        req.eventType = eventType;
        req.userId = userId;
        req.sessionId = sessionId;
        req.productId = productId;
        req.searchKeyword = searchKeyword;
        req.pageUrl = pageUrl;
        req.errorMessage = errorMessage;
        return req;
    }

    public EventLog toDomain() {
        return EventLog.builder()
                .eventType(eventType)
                .userId(userId)
                .sessionId(sessionId)
                .productId(productId)
                .searchKeyword(searchKeyword)
                .pageUrl(pageUrl)
                .errorMessage(errorMessage)
                .build();
    }
}