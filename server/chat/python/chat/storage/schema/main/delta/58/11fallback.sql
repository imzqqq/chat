CREATE TABLE IF NOT EXISTS e2e_fallback_keys_json (
    user_id TEXT NOT NULL, -- The user this fallback key is for.
    device_id TEXT NOT NULL, -- The device this fallback key is for.
    algorithm TEXT NOT NULL, -- Which algorithm this fallback key is for.
    key_id TEXT NOT NULL, -- An id for suppressing duplicate uploads.
    key_json TEXT NOT NULL, -- The key as a JSON blob.
    used BOOLEAN NOT NULL DEFAULT FALSE, -- Whether the key has been used or not.
    CONSTRAINT e2e_fallback_keys_json_uniqueness UNIQUE (user_id, device_id, algorithm)
);
