CREATE TABLE stream_positions (
    stream_name TEXT NOT NULL,
    instance_name TEXT NOT NULL,
    stream_id BIGINT NOT NULL
);

CREATE UNIQUE INDEX stream_positions_idx ON stream_positions(stream_name, instance_name);
