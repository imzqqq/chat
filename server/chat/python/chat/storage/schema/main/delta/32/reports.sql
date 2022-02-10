CREATE TABLE event_reports(
    id BIGINT NOT NULL PRIMARY KEY,
    received_ts BIGINT NOT NULL,
    room_id TEXT NOT NULL,
    event_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    reason TEXT,
    content TEXT
);
