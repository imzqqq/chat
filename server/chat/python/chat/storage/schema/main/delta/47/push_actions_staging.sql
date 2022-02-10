-- Temporary staging area for push actions that have been calculated for an
-- event, but the event hasn't yet been persisted.
-- When the event is persisted the rows are moved over to the
-- event_push_actions table.
CREATE TABLE event_push_actions_staging (
    event_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    actions TEXT NOT NULL,
    notif SMALLINT NOT NULL,
    highlight SMALLINT NOT NULL
);

CREATE INDEX event_push_actions_staging_id ON event_push_actions_staging(event_id);
