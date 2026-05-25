package com.liveklass.liveklass.domain;


import com.liveklass.liveklass.constant.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventStatistics {

    private Long id;
    private EventType eventType;

    // 분·초는 항상 00  ex: 2026-05-25 14:00:00
    private LocalDateTime statisticHour;

    private Long eventCount;
}
