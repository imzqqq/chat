-- Holds MSC2918 refresh tokens
CREATE TABLE refresh_tokens (
  id BIGINT PRIMARY KEY,
  user_id TEXT NOT NULL,
  device_id TEXT NOT NULL,
  token TEXT NOT NULL,
  -- When consumed, a new refresh token is generated, which is tracked by
  -- this foreign key
  next_token_id BIGINT REFERENCES refresh_tokens (id) ON DELETE CASCADE,
  UNIQUE(token)
);

-- Add a reference to the refresh token generated alongside each access token
ALTER TABLE "access_tokens"
  ADD COLUMN refresh_token_id BIGINT REFERENCES refresh_tokens (id) ON DELETE CASCADE;

-- Add a flag whether the token was already used or not
ALTER TABLE "access_tokens"
  ADD COLUMN used BOOLEAN;
