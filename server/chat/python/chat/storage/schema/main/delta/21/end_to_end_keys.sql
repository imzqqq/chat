CREATE TABLE IF NOT EXISTS e2e_device_keys_json (
    user_id TEXT NOT NULL, -- The user these keys are for.
    device_id TEXT NOT NULL, -- Which of the user's devices these keys are for.
    ts_added_ms BIGINT NOT NULL, -- When the keys were uploaded.
    key_json TEXT NOT NULL, -- The keys for the device as a JSON blob.
    CONSTRAINT e2e_device_keys_json_uniqueness UNIQUE (user_id, device_id)
);


CREATE TABLE IF NOT EXISTS e2e_one_time_keys_json (
    user_id TEXT NOT NULL, -- The user this one-time key is for.
    device_id TEXT NOT NULL, -- The device this one-time key is for.
    algorithm TEXT NOT NULL, -- Which algorithm this one-time key is for.
    key_id TEXT NOT NULL, -- An id for suppressing duplicate uploads.
    ts_added_ms BIGINT NOT NULL, -- When this key was uploaded.
    key_json TEXT NOT NULL, -- The key as a JSON blob.
    CONSTRAINT e2e_one_time_keys_json_uniqueness UNIQUE (user_id, device_id, algorithm, key_id)
);
