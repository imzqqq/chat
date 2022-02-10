ALTER TABLE users ADD deactivated SMALLINT DEFAULT 0 NOT NULL;

INSERT INTO background_updates (update_name, progress_json) VALUES
  ('users_set_deactivated_flag', '{}');
