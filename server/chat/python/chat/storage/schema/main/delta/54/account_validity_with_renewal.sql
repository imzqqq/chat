-- We previously changed the schema for this table without renaming the file, which means
-- that some databases might still be using the old schema. This ensures Chat uses the
-- right schema for the table.
DROP TABLE IF EXISTS account_validity;

-- Track what users are in public rooms.
CREATE TABLE IF NOT EXISTS account_validity (
    user_id TEXT PRIMARY KEY,
    expiration_ts_ms BIGINT NOT NULL,
    email_sent BOOLEAN NOT NULL,
    renewal_token TEXT
);

CREATE INDEX account_validity_email_sent_idx ON account_validity(email_sent, expiration_ts_ms)
CREATE UNIQUE INDEX account_validity_renewal_string_idx ON account_validity(renewal_token)
