-- Existing rows will default to NULL, so anything reading from these tables
-- needs to interpret NULL as 0. This is fine here as no existing rooms can have
-- any knocked members.
ALTER TABLE room_stats_current ADD COLUMN knocked_members INT;
ALTER TABLE room_stats_historical ADD COLUMN knocked_members BIGINT;
