 -- analyze user_ips, to help ensure the correct indices are used
INSERT INTO background_updates (update_name, progress_json) VALUES
  ('user_ips_analyze', '{}');

-- delete duplicates
INSERT INTO background_updates (update_name, progress_json, depends_on) VALUES
  ('user_ips_remove_dupes', '{}', 'user_ips_analyze');

-- add a new unique index to user_ips table
INSERT INTO background_updates (update_name, progress_json, depends_on) VALUES
  ('user_ips_device_unique_index', '{}', 'user_ips_remove_dupes');

-- drop the old original index
INSERT INTO background_updates (update_name, progress_json, depends_on) VALUES
  ('user_ips_drop_nonunique_index', '{}', 'user_ips_device_unique_index');
