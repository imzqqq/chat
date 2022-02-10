CREATE TABLE state_groups (
    id BIGINT PRIMARY KEY,
    room_id TEXT NOT NULL,
    event_id TEXT NOT NULL
);

CREATE TABLE state_groups_state (
    state_group BIGINT NOT NULL,
    room_id TEXT NOT NULL,
    type TEXT NOT NULL,
    state_key TEXT NOT NULL,
    event_id TEXT NOT NULL
);

CREATE TABLE state_group_edges (
    state_group BIGINT NOT NULL,
    prev_state_group BIGINT NOT NULL
);

CREATE INDEX state_group_edges_idx ON state_group_edges (state_group);
CREATE INDEX state_group_edges_prev_idx ON state_group_edges (prev_state_group);
CREATE INDEX state_groups_state_type_idx ON state_groups_state (state_group, type, state_key);
