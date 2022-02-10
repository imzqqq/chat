/* When we can use this key until, before we have to refresh it. */
ALTER TABLE server_signature_keys ADD COLUMN ts_valid_until_ms BIGINT;

UPDATE server_signature_keys SET ts_valid_until_ms = (
    SELECT MAX(ts_valid_until_ms) FROM server_keys_json skj WHERE
        skj.server_name = server_signature_keys.server_name AND
        skj.key_id = server_signature_keys.key_id
);
