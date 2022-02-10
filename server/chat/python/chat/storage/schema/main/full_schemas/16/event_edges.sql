/* We used to create tables called event_destinations and
 * state_forward_extremities, but these are no longer used and are removed in
 * delta 54.
 */

CREATE TABLE IF NOT EXISTS event_forward_extremities(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    UNIQUE (event_id, room_id)
);

CREATE INDEX ev_extrem_room ON event_forward_extremities(room_id);
CREATE INDEX ev_extrem_id ON event_forward_extremities(event_id);


CREATE TABLE IF NOT EXISTS event_backward_extremities(
    event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    UNIQUE (event_id, room_id)
);

CREATE INDEX ev_b_extrem_room ON event_backward_extremities(room_id);
CREATE INDEX ev_b_extrem_id ON event_backward_extremities(event_id);


CREATE TABLE IF NOT EXISTS event_edges(
    event_id TEXT NOT NULL,
    prev_event_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    is_state BOOL NOT NULL,  -- true if this is a prev_state edge rather than a regular
                             -- event dag edge.
    UNIQUE (event_id, prev_event_id, room_id, is_state)
);

CREATE INDEX ev_edges_id ON event_edges(event_id);
CREATE INDEX ev_edges_prev_id ON event_edges(prev_event_id);


CREATE TABLE IF NOT EXISTS room_depth(
    room_id TEXT NOT NULL,
    min_depth INTEGER NOT NULL,
    UNIQUE (room_id)
);

CREATE INDEX room_depth_room ON room_depth(room_id);

CREATE TABLE IF NOT EXISTS event_auth(
    event_id TEXT NOT NULL,
    auth_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    UNIQUE (event_id, auth_id, room_id)
);

CREATE INDEX evauth_edges_id ON event_auth(event_id);
CREATE INDEX evauth_edges_auth_id ON event_auth(auth_id);
