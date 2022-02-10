CREATE TABLE IF NOT EXISTS redactions (
    event_id TEXT NOT NULL,
    redacts TEXT NOT NULL,
    UNIQUE (event_id)
);

CREATE INDEX redactions_event_id ON redactions (event_id);
CREATE INDEX redactions_redacts ON redactions (redacts);
