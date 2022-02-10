ALTER TABLE local_media_repository ADD COLUMN quarantined_by TEXT;
ALTER TABLE remote_media_cache ADD COLUMN quarantined_by TEXT;
