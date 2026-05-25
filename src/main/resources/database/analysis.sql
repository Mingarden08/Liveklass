-- 이벤트 타입별 발생 횟수
SELECT
    event_type,
    COUNT(*) AS event_count
FROM event_logs
GROUP BY event_type;

-- 시간대별 이벤트 수
SELECT
    HOUR(created_at) AS hour,
    COUNT(*) AS event_count
FROM event_logs
GROUP BY hour
ORDER BY hour;

-- 가장 많이 조회된 상품
SELECT
    product_id,
    COUNT(*) AS view_count
FROM event_logs
WHERE event_type = 'PRODUCT_VIEWED'
GROUP BY product_id
ORDER BY view_count DESC
    LIMIT 5;

-- 에러 이벤트 수
SELECT
    COUNT(*) AS error_count
FROM event_logs
WHERE event_type = 'ERROR_OCCURRED';