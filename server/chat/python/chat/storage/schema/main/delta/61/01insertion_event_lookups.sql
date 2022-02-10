-- Add a table that keeps track of "insertion" events and
-- their next_chunk_id's so we can navigate to the next chunk of history.
CREATE TABLE IF NOT EXISTS insertion_events(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    next_chunk_id TEXT NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS insertion_events_event_id ON insertion_events(event_id);
CREATE INDEX IF NOT EXISTS insertion_events_next_chunk_id ON insertion_events(next_chunk_id);

-- Add a table that keeps track of all of the events we are inserting between.
-- We use this when navigating the DAG and when we hit an event which matches
-- `insertion_prev_event_id`, it should backfill from the "insertion" event and
-- navigate the historical messages from there.
CREATE TABLE IF NOT EXISTS insertion_event_edges(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    insertion_prev_event_id TEXT NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS insertion_event_edges_event_id ON insertion_event_edges(event_id);
CREATE INDEX IF NOT EXISTS insertion_event_edges_insertion_room_id ON insertion_event_edges(room_id);
CREATE INDEX IF NOT EXISTS insertion_event_edges_insertion_prev_event_id ON insertion_event_edges(insertion_prev_event_id);

-- Add a table that keeps track of how each chunk is labeled. The chunks are
-- connected together based on an insertion events `next_chunk_id`.
CREATE TABLE IF NOT EXISTS chunk_events(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    chunk_id TEXT NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS chunk_events_event_id ON chunk_events(event_id);
CREATE INDEX IF NOT EXISTS chunk_events_chunk_id ON chunk_events(chunk_id);
