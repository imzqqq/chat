CREATE TABLE IF NOT EXISTS room_aliases(
    room_alias TEXT NOT NULL,
    room_id TEXT NOT NULL,
    UNIQUE (room_alias)
);

CREATE INDEX room_aliases_id ON room_aliases(room_id);

CREATE TABLE IF NOT EXISTS room_alias_servers(
    room_alias TEXT NOT NULL,
    server TEXT NOT NULL
);

CREATE INDEX room_alias_servers_alias ON room_alias_servers(room_alias);
