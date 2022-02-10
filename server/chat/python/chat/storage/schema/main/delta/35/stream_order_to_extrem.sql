CREATE TABLE stream_ordering_to_exterm (
    stream_ordering BIGINT NOT NULL,
    room_id TEXT NOT NULL,
    event_id TEXT NOT NULL
);

INSERT INTO stream_ordering_to_exterm (stream_ordering, room_id, event_id)
    SELECT stream_ordering, room_id, event_id FROM event_forward_extremities
    INNER JOIN (
        SELECT room_id, max(stream_ordering) as stream_ordering FROM events
        INNER JOIN event_forward_extremities USING (room_id, event_id)
        GROUP BY room_id
    ) AS rms USING (room_id);

CREATE INDEX stream_ordering_to_exterm_idx on stream_ordering_to_exterm(
    stream_ordering
);

CREATE INDEX stream_ordering_to_exterm_rm_idx on stream_ordering_to_exterm(
    room_id, stream_ordering
);
