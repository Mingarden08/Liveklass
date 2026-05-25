package com.liveklass.liveklass.domain;

import com.liveklass.liveklass.constant.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventLog {

    private Long id;
    private EventType eventType;
    private String userId;
    private String sessionId;
    private String productId;
    private String searchKeyword;
    private String pageUrl;
    private String errorMessage;
    private LocalDateTime createdAt;
}
