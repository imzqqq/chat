CREATE TABLE federation_stream_position(
    type TEXT NOT NULL,
    stream_id INTEGER NOT NULL
);

INSERT INTO federation_stream_position (type, stream_id) VALUES ('federation', -1);
INSERT INTO federation_stream_position (type, stream_id) SELECT 'events', coalesce(max(stream_ordering), -1) FROM events;
