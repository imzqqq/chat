/* We used to create tables called event_content_hashes and event_edge_hashes,
 * but these are no longer used and are removed in delta 54.
 */

CREATE TABLE IF NOT EXISTS event_reference_hashes (
    event_id TEXT,
    algorithm TEXT,
    hash bytea,
    UNIQUE (event_id, algorithm)
);

CREATE INDEX event_reference_hashes_id ON event_reference_hashes(event_id);


CREATE TABLE IF NOT EXISTS event_signatures (
    event_id TEXT,
    signature_name TEXT,
    key_id TEXT,
    signature bytea,
    UNIQUE (event_id, signature_name, key_id)
);

CREATE INDEX event_signatures_id ON event_signatures(event_id);
