CREATE TABLE IF NOT EXISTS sessions(
    session_type TEXT NOT NULL,  -- The unique key for this type of session.
    session_id TEXT NOT NULL,  -- The session ID passed to the client.
    value TEXT NOT NULL, -- A JSON dictionary to persist.
    expiry_time_ms BIGINT NOT NULL,  -- The time this session will expire (epoch time in milliseconds).
    UNIQUE (session_type, session_id)
);
