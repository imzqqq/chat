-- Records whether the server thinks that the remote users cached device lists
-- may be out of date (e.g. if we have received a to device message from a
-- device we don't know about).
CREATE TABLE IF NOT EXISTS device_lists_remote_resync (
    user_id TEXT NOT NULL,
    added_ts BIGINT NOT NULL
);

CREATE UNIQUE INDEX device_lists_remote_resync_idx ON device_lists_remote_resync (user_id);
CREATE INDEX device_lists_remote_resync_ts_idx ON device_lists_remote_resync (added_ts);
