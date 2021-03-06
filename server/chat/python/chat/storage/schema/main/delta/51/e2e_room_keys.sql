-- users' optionally backed up encrypted e2e sessions
CREATE TABLE e2e_room_keys (
    user_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    session_id TEXT NOT NULL,
    version TEXT NOT NULL,
    first_message_index INT,
    forwarded_count INT,
    is_verified BOOLEAN,
    session_data TEXT NOT NULL
);

CREATE UNIQUE INDEX e2e_room_keys_idx ON e2e_room_keys(user_id, room_id, session_id);

-- the metadata for each generation of encrypted e2e session backups
CREATE TABLE e2e_room_keys_versions (
    user_id TEXT NOT NULL,
    version TEXT NOT NULL,
    algorithm TEXT NOT NULL,
    auth_data TEXT NOT NULL,
    deleted SMALLINT DEFAULT 0 NOT NULL
);

CREATE UNIQUE INDEX e2e_room_keys_versions_idx ON e2e_room_keys_versions(user_id, version);
