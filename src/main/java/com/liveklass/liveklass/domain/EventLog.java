package com.liveklass.liveklass.domain;

import com.liveklass.liveklass.constant.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventLog {

    private Long id;
    private EventType eventType;
    private String userId;
    private String productId;
    private String searchKeyword;
    private String pageUrl;
    private String errorMessage;
    private LocalDateTime createdAt;
}
