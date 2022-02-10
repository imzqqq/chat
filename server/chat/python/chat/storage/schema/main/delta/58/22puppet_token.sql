-- Whether the access token is an admin token for controlling another user.
ALTER TABLE access_tokens ADD COLUMN puppets_user_id TEXT;
