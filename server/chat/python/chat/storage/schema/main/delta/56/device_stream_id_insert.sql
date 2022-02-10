-- This line already existed in deltas/35/device_stream_id but was not included in the
-- 54 full schema SQL. Add some SQL here to insert the missing row if it does not exist
INSERT INTO device_max_stream_id (stream_id) SELECT 0 WHERE NOT EXISTS (
    SELECT * from device_max_stream_id
);