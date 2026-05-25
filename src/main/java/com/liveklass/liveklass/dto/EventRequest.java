package com.liveklass.liveklass.dto;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.domain.EventLog;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {

    @NotNull(message = "이벤트 타입은 필수입니다.")
    private EventType eventType;

    private Long userId;
    private String sessionId;
    private Long productId;
    private String searchKeyword;
    private String pageUrl;
    private String errorMessage;

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