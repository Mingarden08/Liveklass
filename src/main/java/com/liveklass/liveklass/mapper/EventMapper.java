package com.liveklass.liveklass.mapper;

import com.liveklass.liveklass.dto.EventRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EventMapper {

    void insertEventLog(EventRequest message);

    void upsertEventStatistics(
            @Param("eventType") String eventType
    );
}