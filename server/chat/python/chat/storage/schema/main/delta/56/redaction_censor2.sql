ALTER TABLE redactions ADD COLUMN received_ts BIGINT;

INSERT INTO background_updates (update_name, progress_json) VALUES
  ('redactions_received_ts', '{}');

INSERT INTO background_updates (update_name, progress_json) VALUES
  ('redactions_have_censored_ts_idx', '{}');
