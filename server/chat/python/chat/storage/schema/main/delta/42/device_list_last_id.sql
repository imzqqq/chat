-- Table of last stream_id that we sent to destination for user_id. This is
-- used to fill out the `prev_id` fields of outbound device list updates.
CREATE TABLE device_lists_outbound_last_success (
    destination TEXT NOT NULL,
    user_id TEXT NOT NULL,
    stream_id BIGINT NOT NULL
);

INSERT INTO device_lists_outbound_last_success
    SELECT destination, user_id, coalesce(max(stream_id), 0) as stream_id
        FROM device_lists_outbound_pokes
        WHERE sent = (1 = 1)  -- sqlite doesn't have inbuilt boolean values
        GROUP BY destination, user_id;

CREATE INDEX device_lists_outbound_last_success_idx ON device_lists_outbound_last_success(
    destination, user_id, stream_id
);
