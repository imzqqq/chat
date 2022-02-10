CREATE TABLE IF NOT EXISTS new_server_keys_json (
    server_name TEXT NOT NULL, -- Server name.
    key_id TEXT NOT NULL, -- Requested key id.
    from_server TEXT NOT NULL, -- Which server the keys were fetched from.
    ts_added_ms BIGINT NOT NULL, -- When the keys were fetched
    ts_valid_until_ms BIGINT NOT NULL, -- When this version of the keys exipires.
    key_json bytea NOT NULL, -- JSON certificate for the remote server.
    CONSTRAINT server_keys_json_uniqueness UNIQUE (server_name, key_id, from_server)
);

INSERT INTO new_server_keys_json
    SELECT server_name, key_id, from_server,ts_added_ms, ts_valid_until_ms, key_json FROM server_keys_json ;

DROP TABLE server_keys_json;

ALTER TABLE new_server_keys_json RENAME TO server_keys_json;
