-- This originally was in 58/, but landed after 59/ was created, and so some
-- servers running develop didn't run this delta. Running it again should be
-- safe.
--
-- We first delete any in progress `rejected_events_metadata` background update,
-- to ensure that we don't conflict when trying to insert the new one. (We could
-- alternatively do an ON CONFLICT DO NOTHING, but that syntax isn't supported
-- by older SQLite versions. Plus, this should be a rare case).
DELETE FROM background_updates WHERE update_name = 'rejected_events_metadata';
INSERT INTO background_updates (ordering, update_name, progress_json) VALUES
  (5828, 'rejected_events_metadata', '{}');
