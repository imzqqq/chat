CREATE TABLE user_daily_visits ( user_id TEXT NOT NULL,
                                 device_id TEXT,
                                 timestamp BIGINT NOT NULL );
CREATE INDEX user_daily_visits_uts_idx ON user_daily_visits(user_id, timestamp);
CREATE INDEX user_daily_visits_ts_idx ON user_daily_visits(timestamp);
