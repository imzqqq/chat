CREATE TABLE IF NOT EXISTS background_updates(
    update_name TEXT NOT NULL, -- The name of the background update.
    progress_json TEXT NOT NULL, -- The current progress of the update as JSON.
    CONSTRAINT background_updates_uniqueness UNIQUE (update_name)
);
