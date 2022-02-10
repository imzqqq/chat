-- Start a background job to cleanup extremities that were incorrectly added
-- by bug #5269.
INSERT INTO background_updates (update_name, progress_json) VALUES
  ('delete_soft_failed_extremities', '{}');

DROP TABLE IF EXISTS _extremities_to_check;  -- To make this delta schema file idempotent.
CREATE TABLE _extremities_to_check AS SELECT event_id FROM event_forward_extremities;
CREATE INDEX _extremities_to_check_id ON _extremities_to_check(event_id);
