-- A subset of remote users whose profiles we have cached.
-- Whether a user is in this table or not is defined by the storage function
-- `is_subscribed_remote_profile_for_user`
CREATE TABLE remote_profile_cache (
    user_id TEXT NOT NULL,
    displayname TEXT,
    avatar_url TEXT,
    last_check BIGINT NOT NULL
);

CREATE UNIQUE INDEX remote_profile_cache_user_id ON remote_profile_cache(user_id);
CREATE INDEX remote_profile_cache_time ON remote_profile_cache(last_check);
