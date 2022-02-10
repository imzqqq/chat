-- version is supposed to be part of the room keys index
CREATE UNIQUE INDEX e2e_room_keys_with_version_idx ON e2e_room_keys(user_id, version, room_id, session_id);
DROP INDEX IF EXISTS e2e_room_keys_idx;
