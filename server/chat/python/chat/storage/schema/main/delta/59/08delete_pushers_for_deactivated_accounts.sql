-- We may not have deleted all pushers for deactivated accounts, so we set up a
-- background job to delete them.
INSERT INTO background_updates (ordering, update_name, progress_json) VALUES
  (5908, 'remove_deactivated_pushers', '{}');
