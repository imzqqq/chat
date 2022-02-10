-- Aggregate of old notification counts that have been deleted out of the
-- main event_push_actions table. This count does not include those that were
-- highlights, as they remain in the event_push_actions table.
CREATE TABLE event_push_summary (
    user_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    notif_count BIGINT NOT NULL,
    stream_ordering BIGINT NOT NULL
);

CREATE INDEX event_push_summary_user_rm ON event_push_summary(user_id, room_id);


-- The stream ordering up to which we have aggregated the event_push_actions
-- table into event_push_summary
CREATE TABLE event_push_summary_stream_ordering (
    Lock CHAR(1) NOT NULL DEFAULT 'X' UNIQUE,  -- Makes sure this table only has one row.
    stream_ordering BIGINT NOT NULL,
    CHECK (Lock='X')
);

INSERT INTO event_push_summary_stream_ordering (stream_ordering) VALUES (0);
