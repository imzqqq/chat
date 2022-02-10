CREATE TABLE IF NOT EXISTS receipts_graph(
    room_id TEXT NOT NULL,
    receipt_type TEXT NOT NULL,
    user_id TEXT NOT NULL,
    event_ids TEXT NOT NULL,
    data TEXT NOT NULL,
    CONSTRAINT receipts_graph_uniqueness UNIQUE (room_id, receipt_type, user_id)
);

CREATE TABLE IF NOT EXISTS receipts_linearized (
    stream_id BIGINT NOT NULL,
    room_id TEXT NOT NULL,
    receipt_type TEXT NOT NULL,
    user_id TEXT NOT NULL,
    event_id TEXT NOT NULL,
    data TEXT NOT NULL,
    CONSTRAINT receipts_linearized_uniqueness UNIQUE (room_id, receipt_type, user_id)
);

CREATE INDEX receipts_linearized_id ON receipts_linearized(
  stream_id
);
