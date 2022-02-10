-- The last time this access token was "validated" (i.e. logged in or succeeded
-- at user-interactive authentication).
ALTER TABLE access_tokens ADD COLUMN last_validated BIGINT;
