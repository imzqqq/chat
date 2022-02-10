-- cross-signing keys
CREATE TABLE IF NOT EXISTS e2e_cross_signing_keys (
    user_id TEXT NOT NULL,
    -- the type of cross-signing key (master, user_signing, or self_signing)
    keytype TEXT NOT NULL,
    -- the full key information, as a json-encoded dict
    keydata TEXT NOT NULL,
    -- for keeping the keys in order, so that we can fetch the latest one
    stream_id BIGINT NOT NULL
);

CREATE UNIQUE INDEX e2e_cross_signing_keys_idx ON e2e_cross_signing_keys(user_id, keytype, stream_id);

-- cross-signing signatures
CREATE TABLE IF NOT EXISTS e2e_cross_signing_signatures (
    -- user who did the signing
    user_id TEXT NOT NULL,
    -- key used to sign
    key_id TEXT NOT NULL,
    -- user who was signed
    target_user_id TEXT NOT NULL,
    -- device/key that was signed
    target_device_id TEXT NOT NULL,
    -- the actual signature
    signature TEXT NOT NULL
);

-- replaced by the index created in signing_keys_nonunique_signatures.sql
-- CREATE UNIQUE INDEX e2e_cross_signing_signatures_idx ON e2e_cross_signing_signatures(user_id, target_user_id, target_device_id);

-- stream of user signature updates
CREATE TABLE IF NOT EXISTS user_signature_stream (
    -- uses the same stream ID as device list stream
    stream_id BIGINT NOT NULL,
    -- user who did the signing
    from_user_id TEXT NOT NULL,
    -- list of users who were signed, as a JSON array
    user_ids TEXT NOT NULL
);

CREATE UNIQUE INDEX user_signature_stream_idx ON user_signature_stream(stream_id);
