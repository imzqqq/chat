-- A shadow-banned user may be told that their requests succeeded when they were
-- actually ignored.
ALTER TABLE users ADD COLUMN shadow_banned BOOLEAN;
