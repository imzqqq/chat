CREATE TABLE state_group_edges(
    state_group BIGINT NOT NULL,
    prev_state_group BIGINT NOT NULL
);

CREATE INDEX state_group_edges_idx ON state_group_edges(state_group);
CREATE INDEX state_group_edges_prev_idx ON state_group_edges(prev_state_group);
