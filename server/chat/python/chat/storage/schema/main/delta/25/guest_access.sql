/*
 * This is a manual index of guest_access content of state events,
 * so that we can join on them in SELECT statements.
 */
CREATE TABLE IF NOT EXISTS guest_access(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    guest_access TEXT NOT NULL,
    UNIQUE (event_id)
);
