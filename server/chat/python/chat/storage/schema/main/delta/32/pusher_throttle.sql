CREATE TABLE pusher_throttle(
    pusher BIGINT NOT NULL,
    room_id TEXT NOT NULL,
    last_sent_ts BIGINT,
    throttle_ms BIGINT,
    PRIMARY KEY (pusher, room_id)
);
