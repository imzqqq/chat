CREATE TABLE IF NOT EXISTS event_expiry (
    event_id TEXT PRIMARY KEY,
    expiry_ts BIGINT NOT NULL
);

CREATE INDEX event_expiry_expiry_ts_idx ON event_expiry(expiry_ts);
