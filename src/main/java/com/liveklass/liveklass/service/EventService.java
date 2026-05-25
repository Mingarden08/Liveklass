package com.liveklass.liveklass.service;

import com.liveklass.liveklass.constant.EventType;
import com.liveklass.liveklass.dto.EventRequest;
import com.liveklass.liveklass.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;

    @Transactional
    public void processEvent(EventRequest message) {

        eventMapper.insertEventLog(message);

        if (message.getEventType() == EventType.SEARCH_EXECUTED
                && message.getSearchKeyword() != null
                && !message.getSearchKeyword().isBlank()) {
            eventMapper.upsertSearchKeywordStatistics(message.getSearchKeyword());
        }

        if (message.getEventType() == EventType.PAGE_VIEWED
                && message.getPageUrl() != null
                && !message.getPageUrl().isBlank()) {
            eventMapper.upsertPageViewStatistics(message.getPageUrl());
        }
    }
}