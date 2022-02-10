-- a table of monthly active users, for use where blocking based on mau limits
CREATE TABLE monthly_active_users (
    user_id TEXT NOT NULL,
    -- Last time we saw the user. Not guaranteed to be accurate due to rate limiting
    -- on updates, Granularity of updates governed by
    -- chat.storage.monthly_active_users.LAST_SEEN_GRANULARITY
    -- Measured in ms since epoch.
    timestamp BIGINT NOT NULL
);

CREATE UNIQUE INDEX monthly_active_users_users ON monthly_active_users(user_id);
CREATE INDEX monthly_active_users_time_stamp ON monthly_active_users(timestamp);
