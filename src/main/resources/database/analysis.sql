이벤트 타입별 발생 수 - pie Chart
SELECT
    event_type AS metric,
    COUNT(*) AS value
FROM event_logs
GROUP BY event_type;

시간대별 이벤트 수 - Time Series
SELECT
    created_at AS time,
    COUNT(*) AS value
FROM event_logs
GROUP BY created_at
ORDER BY created_at;

인기 상품 - Bar Chart
SELECT
    product_id,
    COUNT(*) AS view_count
FROM event_logs
WHERE event_type = 'PRODUCT_VIEWED'
GROUP BY product_id
ORDER BY view_count DESC
LIMIT 5;