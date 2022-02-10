-- Tracks which identity server a user bound their threepid via.
CREATE TABLE user_threepid_id_server (
    user_id TEXT NOT NULL,
    medium TEXT NOT NULL,
    address TEXT NOT NULL,
    id_server TEXT NOT NULL
);

CREATE UNIQUE INDEX user_threepid_id_server_idx ON user_threepid_id_server(
    user_id, medium, address, id_server
);

INSERT INTO background_updates (update_name, progress_json) VALUES
  ('user_threepids_grandfather', '{}');
