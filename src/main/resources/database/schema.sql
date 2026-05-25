create database liveklass;

use liveklass;

CREATE TABLE event_logs (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            event_type VARCHAR(50) NOT NULL,
                            user_id CHAR(5),
                            session_id VARCHAR(255),
                            product_id CHAR(5),
                            search_keyword VARCHAR(255),
                            page_url VARCHAR(255),
                            error_message TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_statistics (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  event_type VARCHAR(50) NOT NULL,
                                  statistic_date DATE NOT NULL,
                                  event_count BIGINT NOT NULL DEFAULT 0,
                                  UNIQUE KEY uk_event_date (
                                      event_type,
                                      statistic_date
                                      )
);

CREATE INDEX idx_event_type ON event_logs (event_type);
CREATE INDEX idx_created_at ON event_logs (created_at);
CREATE INDEX idx_statistic_hour ON event_statistics (statistic_hour);