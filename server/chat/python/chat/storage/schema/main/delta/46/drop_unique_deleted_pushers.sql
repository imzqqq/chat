-- drop the unique constraint on deleted_pushers so that we can just insert
-- into it rather than upserting.

CREATE TABLE deleted_pushers2 (
    stream_id BIGINT NOT NULL,
    app_id TEXT NOT NULL,
    pushkey TEXT NOT NULL,
    user_id TEXT NOT NULL
);

INSERT INTO deleted_pushers2 (stream_id, app_id, pushkey, user_id)
    SELECT stream_id, app_id, pushkey, user_id from deleted_pushers;

DROP TABLE deleted_pushers;
ALTER TABLE deleted_pushers2 RENAME TO deleted_pushers;

-- create the index after doing the inserts because that's more efficient.
-- it also means we can give it the same name as the old one without renaming.
CREATE INDEX deleted_pushers_stream_id ON deleted_pushers (stream_id);

