-- Add a table that keeps track of which "insertion" events need to be backfilled
CREATE TABLE IF NOT EXISTS insertion_event_extremities(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS insertion_event_extremities_event_id ON insertion_event_extremities(event_id);
CREATE INDEX IF NOT EXISTS insertion_event_extremities_room_id ON insertion_event_extremities(room_id);
