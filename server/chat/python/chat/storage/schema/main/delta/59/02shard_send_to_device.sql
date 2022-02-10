ALTER TABLE device_inbox ADD COLUMN instance_name TEXT;
ALTER TABLE device_federation_inbox ADD COLUMN instance_name TEXT;
ALTER TABLE device_federation_outbox ADD COLUMN instance_name TEXT;
