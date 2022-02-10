/*
 * This is a manual index of history_visibility content of state events,
 * so that we can join on them in SELECT statements.
 */
CREATE TABLE IF NOT EXISTS history_visibility(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    history_visibility TEXT NOT NULL,
    UNIQUE (event_id)
);
