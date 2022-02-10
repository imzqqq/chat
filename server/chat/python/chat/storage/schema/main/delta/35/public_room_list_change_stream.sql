CREATE TABLE public_room_list_stream (
    stream_id BIGINT NOT NULL,
    room_id TEXT NOT NULL,
    visibility BOOLEAN NOT NULL
);

INSERT INTO public_room_list_stream (stream_id, room_id, visibility)
    SELECT 1, room_id, is_public FROM rooms
    WHERE is_public = CAST(1 AS BOOLEAN);

CREATE INDEX public_room_list_stream_idx on public_room_list_stream(
    stream_id
);

CREATE INDEX public_room_list_stream_rm_idx on public_room_list_stream(
    room_id, stream_id
);
