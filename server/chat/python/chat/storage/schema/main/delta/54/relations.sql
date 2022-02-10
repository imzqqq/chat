-- Tracks related events, like reactions, replies, edits, etc. Note that things
-- in this table are not necessarily "valid", e.g. it may contain edits from
-- people who don't have power to edit other peoples events.
CREATE TABLE IF NOT EXISTS event_relations (
    event_id TEXT NOT NULL,
    relates_to_id TEXT NOT NULL,
    relation_type TEXT NOT NULL,
    aggregation_key TEXT
);

CREATE UNIQUE INDEX event_relations_id ON event_relations(event_id);
CREATE INDEX event_relations_relates ON event_relations(relates_to_id, relation_type, aggregation_key);
