-- We want to start storing the room version independently of
-- `current_state_events` so that we can delete stale entries from it without
-- losing the information.
ALTER TABLE rooms ADD COLUMN room_version TEXT;


INSERT into background_updates (update_name, progress_json)
    VALUES ('add_rooms_room_version_column', '{}');
