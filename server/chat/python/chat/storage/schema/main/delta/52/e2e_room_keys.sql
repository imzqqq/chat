/* Change version column to an integer so we can do MAX() sensibly
 */
CREATE TABLE e2e_room_keys_versions_new (
    user_id TEXT NOT NULL,
    version BIGINT NOT NULL,
    algorithm TEXT NOT NULL,
    auth_data TEXT NOT NULL,
    deleted SMALLINT DEFAULT 0 NOT NULL
);

INSERT INTO e2e_room_keys_versions_new
    SELECT user_id, CAST(version as BIGINT), algorithm, auth_data, deleted FROM e2e_room_keys_versions;

DROP TABLE e2e_room_keys_versions;
ALTER TABLE e2e_room_keys_versions_new RENAME TO e2e_room_keys_versions;

CREATE UNIQUE INDEX e2e_room_keys_versions_idx ON e2e_room_keys_versions(user_id, version);

/* Change e2e_rooms_keys to match
 */
CREATE TABLE e2e_room_keys_new (
    user_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    session_id TEXT NOT NULL,
    version BIGINT NOT NULL,
    first_message_index INT,
    forwarded_count INT,
    is_verified BOOLEAN,
    session_data TEXT NOT NULL
);

INSERT INTO e2e_room_keys_new
    SELECT user_id, room_id, session_id, CAST(version as BIGINT), first_message_index, forwarded_count, is_verified, session_data FROM e2e_room_keys;

DROP TABLE e2e_room_keys;
ALTER TABLE e2e_room_keys_new RENAME TO e2e_room_keys;

CREATE UNIQUE INDEX e2e_room_keys_idx ON e2e_room_keys(user_id, room_id, session_id);
