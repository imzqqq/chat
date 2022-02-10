-- register a background update which will recreate the
-- local_media_repository_url_idx index.
--
-- We do this as a bg update not because it is a particularly onerous
-- operation, but because we'd like it to be a partial index if possible, and
-- the background_index_update code will understand whether we are on
-- postgres or sqlite and behave accordingly.
INSERT INTO background_updates (update_name, progress_json) VALUES
    ('local_media_repository_url_idx', '{}');
