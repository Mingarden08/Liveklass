package com.liveklass.liveklass.service;

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

        eventMapper.upsertEventStatistics(
                message.getEventType().name()
        );
    }
}