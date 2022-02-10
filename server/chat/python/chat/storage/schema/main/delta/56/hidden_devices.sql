-- device list needs to know which ones are "real" devices, and which ones are
-- just used to avoid collisions
ALTER TABLE devices ADD COLUMN hidden BOOLEAN DEFAULT FALSE;
