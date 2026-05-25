이벤트 타입별 발생 수 - pie Chart
SELECT
    event_type AS metric,
    COUNT(*) AS value
FROM event_logs
GROUP BY event_type;

하루 동안 가장 많이 방문한 URL - Bar Chart
SELECT
    page_url AS metric,
    SUM(event_count) AS value
FROM page_view_statistics
WHERE statistic_date = DATE(DATE_ADD(UTC_TIMESTAMP(), INTERVAL 9 HOUR))
GROUP BY page_url
ORDER BY value DESC

검색어 Top 3 - Bar Chart
SELECT
    search_keyword AS metric,
    SUM(event_count) AS value
FROM search_keyword_statistics
GROUP BY search_keyword
ORDER BY value DESC
LIMIT 3;