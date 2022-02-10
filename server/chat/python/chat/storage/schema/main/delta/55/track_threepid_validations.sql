CREATE TABLE IF NOT EXISTS threepid_validation_session (
    session_id TEXT PRIMARY KEY,
    medium TEXT NOT NULL,
    address TEXT NOT NULL,
    client_secret TEXT NOT NULL,
    last_send_attempt BIGINT NOT NULL,
    validated_at BIGINT
);

CREATE TABLE IF NOT EXISTS threepid_validation_token (
    token TEXT PRIMARY KEY,
    session_id TEXT NOT NULL,
    next_link TEXT,
    expires BIGINT NOT NULL
);

CREATE INDEX threepid_validation_token_session_id ON threepid_validation_token(session_id);
