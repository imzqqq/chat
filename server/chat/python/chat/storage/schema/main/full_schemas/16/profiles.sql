CREATE TABLE IF NOT EXISTS profiles(
    user_id TEXT NOT NULL,
    displayname TEXT,
    avatar_url TEXT,
    UNIQUE(user_id)
);
