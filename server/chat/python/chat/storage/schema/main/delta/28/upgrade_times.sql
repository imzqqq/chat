/*
 * Stores the timestamp when a user upgraded from a guest to a full user, if
 * that happened.
 */

ALTER TABLE users ADD COLUMN upgrade_ts BIGINT;
