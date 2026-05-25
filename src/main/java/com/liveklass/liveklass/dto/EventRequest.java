package com.liveklass.liveklass.dto;

import com.liveklass.liveklass.constant.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class EventRequest {

    @NotNull(message = "이벤트 타입은 필수입니다.")
    private EventType eventType;

    private String userId;
    private String productId;
    private String searchKeyword;
    private String pageUrl;
    private String errorMessage;

}