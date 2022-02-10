CREATE TABLE IF NOT EXISTS appservice_stream_position(
    Lock CHAR(1) NOT NULL DEFAULT 'X' UNIQUE,  -- Makes sure this table only has one row.
    stream_ordering BIGINT,
    CHECK (Lock='X')
);

INSERT INTO appservice_stream_position (stream_ordering)
    SELECT COALESCE(MAX(stream_ordering), 0) FROM events;
