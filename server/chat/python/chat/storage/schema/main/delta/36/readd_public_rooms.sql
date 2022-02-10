-- Re-add some entries to stream_ordering_to_exterm that were incorrectly deleted
INSERT INTO stream_ordering_to_exterm (stream_ordering, room_id, event_id)
    SELECT
        (SELECT stream_ordering FROM events where event_id = e.event_id) AS stream_ordering,
        room_id,
        event_id
    FROM event_forward_extremities AS e
    WHERE NOT EXISTS (
        SELECT room_id FROM stream_ordering_to_exterm AS s
        WHERE s.room_id = e.room_id
    );
