-- Track last seen information for a device in the devices table, rather
-- than relying on it being in the user_ips table (which we want to be able
-- to purge old entries from)
ALTER TABLE devices ADD COLUMN last_seen BIGINT;
ALTER TABLE devices ADD COLUMN ip TEXT;
ALTER TABLE devices ADD COLUMN user_agent TEXT;

INSERT INTO background_updates (update_name, progress_json) VALUES
  ('devices_last_seen', '{}');
