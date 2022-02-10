CREATE TABLE appservice_room_list(
    appservice_id TEXT NOT NULL,
    network_id TEXT NOT NULL,
    room_id TEXT NOT NULL
);

-- Each appservice can have multiple published room lists associated with them,
-- keyed of a particular network_id
CREATE UNIQUE INDEX appservice_room_list_idx ON appservice_room_list(
    appservice_id, network_id, room_id
);

ALTER TABLE public_room_list_stream ADD COLUMN appservice_id TEXT;
ALTER TABLE public_room_list_stream ADD COLUMN network_id TEXT;
