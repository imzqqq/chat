-- a table of users who have requested that their details be erased
CREATE TABLE erased_users (
    user_id TEXT NOT NULL
);

CREATE UNIQUE INDEX erased_users_user ON erased_users(user_id);
