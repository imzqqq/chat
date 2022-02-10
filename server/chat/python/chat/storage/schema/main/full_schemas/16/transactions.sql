-- Stores what transaction ids we have received and what our response was
CREATE TABLE IF NOT EXISTS received_transactions(
    transaction_id TEXT,
    origin TEXT,
    ts BIGINT,
    response_code INTEGER,
    response_json bytea,
    has_been_referenced smallint default 0, -- Whether thishas been referenced by a prev_tx
    UNIQUE (transaction_id, origin)
);

CREATE INDEX transactions_have_ref ON received_transactions(origin, has_been_referenced);-- WHERE has_been_referenced = 0;

-- For sent transactions only.
CREATE TABLE IF NOT EXISTS transaction_id_to_pdu(
    transaction_id INTEGER,
    destination TEXT,
    pdu_id TEXT,
    pdu_origin TEXT,
    UNIQUE (transaction_id, destination)
);

CREATE INDEX transaction_id_to_pdu_dest ON transaction_id_to_pdu(destination);

-- To track destination health
CREATE TABLE IF NOT EXISTS destinations(
    destination TEXT PRIMARY KEY,
    retry_last_ts BIGINT,
    retry_interval INTEGER
);
