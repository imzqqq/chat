-- Adds an index on room_memberships for fetching all forgotten rooms for a user
INSERT INTO background_updates (update_name, progress_json) VALUES
  ('room_membership_forgotten_idx', '{}');
