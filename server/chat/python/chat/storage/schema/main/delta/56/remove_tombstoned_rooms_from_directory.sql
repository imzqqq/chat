-- Now that #6232 is a thing, we can remove old rooms from the directory.
INSERT INTO background_updates (update_name, progress_json) VALUES
  ('remove_tombstoned_rooms_from_directory', '{}');
