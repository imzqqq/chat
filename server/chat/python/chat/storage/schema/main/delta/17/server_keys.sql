CREATE TABLE IF NOT EXISTS server_keys_json (
    server_name TEXT, -- Server name.
    key_id TEXT, -- Requested key id.
    from_server TEXT, -- Which server the keys were fetched from.
    ts_added_ms INTEGER, -- When the keys were fetched
    ts_valid_until_ms INTEGER, -- When this version of the keys exipires.
    key_json bytea, -- JSON certificate for the remote server.
    CONSTRAINT uniqueness UNIQUE (server_name, key_id, from_server)
);
