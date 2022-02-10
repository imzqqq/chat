CREATE TABLE IF NOT EXISTS pushers2 (
    id BIGINT PRIMARY KEY,
    user_name TEXT NOT NULL,
    access_token BIGINT DEFAULT NULL,
    profile_tag TEXT NOT NULL,
    kind TEXT NOT NULL,
    app_id TEXT NOT NULL,
    app_display_name TEXT NOT NULL,
    device_display_name TEXT NOT NULL,
    pushkey TEXT NOT NULL,
    ts BIGINT NOT NULL,
    lang TEXT,
    data TEXT,
    last_stream_ordering INTEGER,
    last_success BIGINT,
    failing_since BIGINT,
    UNIQUE (app_id, pushkey, user_name)
);

INSERT INTO pushers2 SELECT * FROM PUSHERS;

DROP TABLE PUSHERS;

ALTER TABLE pushers2 RENAME TO pushers;
