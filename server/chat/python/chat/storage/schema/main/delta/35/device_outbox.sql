DROP TABLE IF EXISTS device_federation_outbox;
CREATE TABLE device_federation_outbox (
    destination TEXT NOT NULL,
    stream_id BIGINT NOT NULL,
    queued_ts BIGINT NOT NULL,
    messages_json TEXT NOT NULL
);


DROP INDEX IF EXISTS device_federation_outbox_destination_id;
CREATE INDEX device_federation_outbox_destination_id
    ON device_federation_outbox(destination, stream_id);


DROP TABLE IF EXISTS device_federation_inbox;
CREATE TABLE device_federation_inbox (
    origin TEXT NOT NULL,
    message_id TEXT NOT NULL,
    received_ts BIGINT NOT NULL
);

DROP INDEX IF EXISTS device_federation_inbox_sender_id;
CREATE INDEX device_federation_inbox_sender_id
    ON device_federation_inbox(origin, message_id);
