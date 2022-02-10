CREATE TABLE ratelimit_override (
    user_id TEXT NOT NULL,
    messages_per_second BIGINT,
    burst_count BIGINT
);

CREATE UNIQUE INDEX ratelimit_override_idx ON ratelimit_override(user_id);
