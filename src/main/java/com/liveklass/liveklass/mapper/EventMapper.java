package com.liveklass.liveklass.mapper;

import com.liveklass.liveklass.dto.EventRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EventMapper {

    void insertEventLog(EventRequest message);

    void upsertPageViewStatistics(
            @Param("pageUrl") String pageUrl
    );

    void upsertSearchKeywordStatistics(
            @Param("searchKeyword") String searchKeyword
    );
}