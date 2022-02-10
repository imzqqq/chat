CREATE TABLE IF NOT EXISTS presence(
  user_id TEXT NOT NULL,
  state VARCHAR(20),
  status_msg TEXT,
  mtime BIGINT, -- miliseconds since last state change
  UNIQUE (user_id)
);

-- For each of /my/ users which possibly-remote users are allowed to see their
-- presence state
CREATE TABLE IF NOT EXISTS presence_allow_inbound(
  observed_user_id TEXT NOT NULL,
  observer_user_id TEXT NOT NULL, -- a UserID,
  UNIQUE (observed_user_id, observer_user_id)
);

-- We used to create a table called presence_list, but this is no longer used
-- and is removed in delta 54.