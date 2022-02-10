CREATE TABLE IF NOT EXISTS application_services_state(
    as_id TEXT PRIMARY KEY,
    state VARCHAR(5),
    last_txn INTEGER
);

CREATE TABLE IF NOT EXISTS application_services_txns(
    as_id TEXT NOT NULL,
    txn_id INTEGER NOT NULL,
    event_ids TEXT NOT NULL,
    UNIQUE(as_id, txn_id)
);

CREATE INDEX IF NOT EXISTS application_services_txns_id ON application_services_txns (
    as_id
);
