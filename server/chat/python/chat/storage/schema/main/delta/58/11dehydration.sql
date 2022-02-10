CREATE TABLE IF NOT EXISTS dehydrated_devices(
    user_id TEXT NOT NULL PRIMARY KEY,
    device_id TEXT NOT NULL,
    device_data TEXT NOT NULL -- JSON-encoded client-defined data
);
