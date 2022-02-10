CREATE TABLE device_max_stream_id (
    stream_id BIGINT NOT NULL
);

INSERT INTO device_max_stream_id (stream_id)
    SELECT COALESCE(MAX(stream_id), 0) FROM device_inbox;
