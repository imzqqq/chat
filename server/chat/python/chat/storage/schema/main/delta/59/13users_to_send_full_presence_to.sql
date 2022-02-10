-- Add a table that keeps track of a list of users who should, upon their next
-- sync request, receive presence for all currently online users that they are
-- "interested" in.

-- The motivation for a DB table over an in-memory list is so that this list
-- can be added to and retrieved from by any worker. Specifically, we don't
-- want to duplicate work across multiple sync workers.

CREATE TABLE IF NOT EXISTS users_to_send_full_presence_to(
    -- The user ID to send full presence to.
    user_id TEXT PRIMARY KEY,
    -- A presence stream ID token - the current presence stream token when the row was last upserted.
    -- If a user calls /sync and this token is part of the update they're to receive, we also include
    -- full user presence in the response.
    -- This allows multiple devices for a user to receive full presence whenever they next call /sync.
    presence_stream_id BIGINT,
    FOREIGN KEY (user_id)
        REFERENCES users (name)
);