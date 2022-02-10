-- Old disused version of the tables below.
DROP TABLE IF EXISTS users_who_share_rooms;

-- Tables keeping track of what users share rooms. This is a map of local users
-- to local or remote users, per room. Remote users cannot be in the user_id
-- column, only the other_user_id column. There are two tables, one for public
-- rooms and those for private rooms.
CREATE TABLE IF NOT EXISTS users_who_share_public_rooms (
    user_id TEXT NOT NULL,
    other_user_id TEXT NOT NULL,
    room_id TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS users_who_share_private_rooms (
    user_id TEXT NOT NULL,
    other_user_id TEXT NOT NULL,
    room_id TEXT NOT NULL
);

CREATE UNIQUE INDEX users_who_share_public_rooms_u_idx ON users_who_share_public_rooms(user_id, other_user_id, room_id);
CREATE INDEX users_who_share_public_rooms_r_idx ON users_who_share_public_rooms(room_id);
CREATE INDEX users_who_share_public_rooms_o_idx ON users_who_share_public_rooms(other_user_id);

CREATE UNIQUE INDEX users_who_share_private_rooms_u_idx ON users_who_share_private_rooms(user_id, other_user_id, room_id);
CREATE INDEX users_who_share_private_rooms_r_idx ON users_who_share_private_rooms(room_id);
CREATE INDEX users_who_share_private_rooms_o_idx ON users_who_share_private_rooms(other_user_id);

-- Make sure that we populate the tables initially by resetting the stream ID
UPDATE user_directory_stream_pos SET stream_id = NULL;
