CREATE TABLE current_state_delta_stream (
    stream_id BIGINT NOT NULL,
    room_id TEXT NOT NULL,
    type TEXT NOT NULL,
    state_key TEXT NOT NULL,
    event_id TEXT,  -- Is null if the key was removed
    prev_event_id TEXT  -- Is null if the key was added
);

CREATE INDEX current_state_delta_stream_idx ON current_state_delta_stream(stream_id);
