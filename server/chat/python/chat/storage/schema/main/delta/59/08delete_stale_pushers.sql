-- Delete all pushers associated with deleted devices. This is to clear up after
-- a bug where they weren't correctly deleted when using workers.
INSERT INTO background_updates (ordering, update_name, progress_json) VALUES
  (5908, 'remove_stale_pushers', '{}');
