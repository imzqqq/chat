-- room_id and topological_ordering are denormalised from the events table in order to
-- make the index work.
CREATE TABLE IF NOT EXISTS event_labels (
    event_id TEXT,
    label TEXT,
    room_id TEXT NOT NULL,
    topological_ordering BIGINT NOT NULL,
    PRIMARY KEY(event_id, label)
);


-- This index enables an event pagination looking for a particular label to index the
-- event_labels table first, which is much quicker than scanning the events table and then
-- filtering by label, if the label is rarely used relative to the size of the room.
CREATE INDEX event_labels_room_id_label_idx ON event_labels(room_id, label, topological_ordering);
