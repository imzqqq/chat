CREATE TABLE IF NOT EXISTS users(
    name TEXT,
    password_hash TEXT,
    creation_ts BIGINT,
    admin SMALLINT DEFAULT 0 NOT NULL,
    UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS access_tokens(
    id BIGINT PRIMARY KEY,
    user_id TEXT NOT NULL,
    device_id TEXT,
    token TEXT NOT NULL,
    last_used BIGINT,
    UNIQUE(token)
);

CREATE TABLE IF NOT EXISTS user_ips (
    user_id TEXT NOT NULL,
    access_token TEXT NOT NULL,
    device_id TEXT,
    ip TEXT NOT NULL,
    user_agent TEXT NOT NULL,
    last_seen BIGINT NOT NULL
);

CREATE INDEX user_ips_user ON user_ips(user_id);
CREATE INDEX user_ips_user_ip ON user_ips(user_id, access_token, ip);
