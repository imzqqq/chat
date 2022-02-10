CREATE TABLE device_inbox (
    user_id TEXT NOT NULL,
    device_id TEXT NOT NULL,
    stream_id BIGINT NOT NULL,
    message_json TEXT NOT NULL -- {"type":, "sender":, "content",}
);

CREATE INDEX device_inbox_user_stream_id ON device_inbox(user_id, device_id, stream_id);
CREATE INDEX device_inbox_stream_id ON device_inbox(stream_id);
