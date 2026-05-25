CREATE TABLE event_logs (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                            event_type VARCHAR(50) NOT NULL,

                            user_id BIGINT,

                            session_id VARCHAR(100),

                            product_id BIGINT,

                            search_keyword VARCHAR(255),

                            page_url VARCHAR(500),

                            error_message TEXT,

                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_statistics (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                  event_type VARCHAR(50) NOT NULL,

                                  statistic_date DATE NOT NULL,

                                  event_count BIGINT NOT NULL,

                                  CONSTRAINT uk_event_statistics
                                      UNIQUE(event_type, statistic_date)
);

CREATE INDEX idx_event_type
    ON event_logs(event_type);

CREATE INDEX idx_created_at
    ON event_logs(created_at);