-- when this access token can be used until, in ms since the epoch. NULL means the token
-- never expires.
ALTER TABLE access_tokens ADD COLUMN valid_until_ms BIGINT;
