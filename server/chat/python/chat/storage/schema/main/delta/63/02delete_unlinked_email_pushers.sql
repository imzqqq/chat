-- We may not have deleted all pushers for emails that are no longer linked
-- to an account, so we set up a background job to delete them.
INSERT INTO background_updates (ordering, update_name, progress_json) VALUES
  (6302, 'remove_deleted_email_pushers', '{}');
