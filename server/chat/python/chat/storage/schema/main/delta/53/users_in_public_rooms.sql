-- We don't need the old version of this table.
DROP TABLE IF EXISTS users_in_public_rooms;

-- Old version of users_in_public_rooms
DROP TABLE IF EXISTS users_who_share_public_rooms;

-- Track what users are in public rooms.
CREATE TABLE IF NOT EXISTS users_in_public_rooms (
    user_id TEXT NOT NULL,
    room_id TEXT NOT NULL
);

CREATE UNIQUE INDEX users_in_public_rooms_u_idx ON users_in_public_rooms(user_id, room_id);
