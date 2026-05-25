use liveklass;

CREATE TABLE event_logs (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            event_type VARCHAR(50) NOT NULL,
                            user_id CHAR(5),
                            product_id CHAR(5),
                            search_keyword VARCHAR(255),
                            page_url VARCHAR(255),
                            error_message TEXT,
                            created_at DATETIME NOT NULL
);

CREATE TABLE search_keyword_statistics (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           search_keyword VARCHAR(255) NOT NULL,
                                           statistic_hour DATETIME NOT NULL,
                                           event_count BIGINT NOT NULL DEFAULT 0,
                                           UNIQUE KEY uk_keyword_hour (
                                               search_keyword,
                                               statistic_hour
                                               ),
                                           INDEX idx_search_keyword (search_keyword),
                                           INDEX idx_statistic_hour (statistic_hour)
);

CREATE TABLE page_view_statistics (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      page_url VARCHAR(255) NOT NULL,
                                      statistic_date DATE NOT NULL,
                                      event_count BIGINT NOT NULL DEFAULT 0,
                                      UNIQUE KEY uk_page_url_date (
                                          page_url,
                                          statistic_date
                                          ),
                                      INDEX idx_page_url (page_url),
                                      INDEX idx_page_view_statistic_date (statistic_date)
);

CREATE INDEX idx_event_type ON event_logs (event_type);
CREATE INDEX idx_created_at ON event_logs (created_at);