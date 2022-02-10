CREATE TABLE devices (
    user_id TEXT NOT NULL,
    device_id TEXT NOT NULL,
    display_name TEXT,
    CONSTRAINT device_uniqueness UNIQUE (user_id, device_id)
);
