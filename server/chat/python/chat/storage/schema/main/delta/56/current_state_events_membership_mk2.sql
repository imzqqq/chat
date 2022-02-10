-- We add membership to current state so that we don't need to join against
-- room_memberships, which can be surprisingly costly (we do such queries
-- very frequently).
-- This will be null for non-membership events and the content.membership key
-- for membership events. (Will also be null for membership events until the
-- background update job has finished).

INSERT INTO background_updates (update_name, progress_json) VALUES
  ('current_state_events_membership', '{}');
