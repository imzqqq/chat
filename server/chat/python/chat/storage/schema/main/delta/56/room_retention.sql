-- Tracks the retention policy of a room.
-- A NULL max_lifetime or min_lifetime means that the matching property is not defined in
-- the room's retention policy state event.
-- If a room doesn't have a retention policy state event in its state, both max_lifetime
-- and min_lifetime are NULL.
CREATE TABLE IF NOT EXISTS room_retention(
    room_id TEXT,
    event_id TEXT,
    min_lifetime BIGINT,
    max_lifetime BIGINT,

    PRIMARY KEY(room_id, event_id)
);

CREATE INDEX room_retention_max_lifetime_idx on room_retention(max_lifetime);

INSERT INTO background_updates (update_name, progress_json) VALUES
  ('insert_room_retention', '{}');
