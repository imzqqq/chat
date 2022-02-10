-- We need to store the stream positions by instance in a sharded config world.
--
-- We default to master as we want the column to be NOT NULL and we correctly
-- reset the instance name to match the config each time we start up.
ALTER TABLE federation_stream_position ADD COLUMN instance_name TEXT NOT NULL DEFAULT 'master';

CREATE UNIQUE INDEX federation_stream_position_instance ON federation_stream_position(type, instance_name);
