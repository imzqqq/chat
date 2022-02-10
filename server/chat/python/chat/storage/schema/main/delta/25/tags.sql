CREATE TABLE IF NOT EXISTS room_tags(
    user_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    tag     TEXT NOT NULL,  -- The name of the tag.
    content TEXT NOT NULL,  -- The JSON content of the tag.
    CONSTRAINT room_tag_uniqueness UNIQUE (user_id, room_id, tag)
);

CREATE TABLE IF NOT EXISTS room_tags_revisions (
    user_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    stream_id BIGINT NOT NULL, -- The current version of the room tags.
    CONSTRAINT room_tag_revisions_uniqueness UNIQUE (user_id, room_id)
);

CREATE TABLE IF NOT EXISTS private_user_data_max_stream_id(
    Lock CHAR(1) NOT NULL DEFAULT 'X' UNIQUE,  -- Makes sure this table only has one row.
    stream_id  BIGINT NOT NULL,
    CHECK (Lock='X')
);

INSERT INTO private_user_data_max_stream_id (stream_id) VALUES (0);
