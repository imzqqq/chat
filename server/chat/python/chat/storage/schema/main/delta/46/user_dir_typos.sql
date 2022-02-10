-- this is just embarassing :|
ALTER TABLE users_in_pubic_room RENAME TO users_in_public_rooms;

-- this is only 300K rows on matrix.org and takes ~3s to generate the index,
-- so is hopefully not going to block anyone else for that long...
CREATE INDEX users_in_public_rooms_room_idx ON users_in_public_rooms(room_id);
CREATE UNIQUE INDEX users_in_public_rooms_user_idx ON users_in_public_rooms(user_id);
DROP INDEX users_in_pubic_room_room_idx;
DROP INDEX users_in_pubic_room_user_idx;
