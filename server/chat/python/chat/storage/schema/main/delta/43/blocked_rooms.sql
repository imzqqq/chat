CREATE TABLE blocked_rooms (
    room_id TEXT NOT NULL,
    user_id TEXT NOT NULL  -- Admin who blocked the room
);

CREATE UNIQUE INDEX blocked_rooms_idx ON blocked_rooms(room_id);
