-- store the current etag of backup version
ALTER TABLE e2e_room_keys_versions ADD COLUMN etag BIGINT;
