CREATE TABLE IF NOT EXISTS deleted_pushers(
    stream_id BIGINT NOT NULL,
    app_id TEXT NOT NULL,
    pushkey TEXT NOT NULL,
    user_id TEXT NOT NULL,
    /* We only track the most recent delete for each app_id, pushkey and user_id. */
    UNIQUE (app_id, pushkey, user_id)
);

CREATE INDEX deleted_pushers_stream_id ON deleted_pushers (stream_id);
