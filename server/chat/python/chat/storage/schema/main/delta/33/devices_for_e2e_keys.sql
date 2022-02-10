-- make sure that we have a device record for each set of E2E keys, so that the
-- user can delete them if they like.
INSERT INTO devices
    SELECT user_id, device_id, NULL FROM e2e_device_keys_json;
