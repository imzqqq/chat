-- See docs/auth_chain_difference_algorithm.md

CREATE TABLE event_auth_chains (
  event_id TEXT PRIMARY KEY,
  chain_id BIGINT NOT NULL,
  sequence_number BIGINT NOT NULL
);

CREATE UNIQUE INDEX event_auth_chains_c_seq_index ON event_auth_chains (chain_id, sequence_number);


CREATE TABLE event_auth_chain_links (
  origin_chain_id BIGINT NOT NULL,
  origin_sequence_number BIGINT NOT NULL,

  target_chain_id BIGINT NOT NULL,
  target_sequence_number BIGINT NOT NULL
);


CREATE INDEX event_auth_chain_links_idx ON event_auth_chain_links (origin_chain_id, target_chain_id);


-- Events that we have persisted but not calculated auth chains for,
-- e.g. out of band memberships (where we don't have the auth chain)
CREATE TABLE event_auth_chain_to_calculate (
  event_id TEXT PRIMARY KEY,
  room_id TEXT NOT NULL,
  type TEXT NOT NULL,
  state_key TEXT NOT NULL
);

CREATE INDEX event_auth_chain_to_calculate_rm_id ON event_auth_chain_to_calculate(room_id);


-- Whether we've calculated the above index for a room.
ALTER TABLE rooms ADD COLUMN has_auth_chain_index BOOLEAN;
