-- this was apparently forgotten when the table was created back in delta 53.
CREATE INDEX users_in_public_rooms_r_idx ON users_in_public_rooms(room_id);
