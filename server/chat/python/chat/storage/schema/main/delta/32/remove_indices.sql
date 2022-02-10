-- The following indices are redundant, other indices are equivalent or
-- supersets
DROP INDEX IF EXISTS events_room_id; -- Prefix of events_room_stream
DROP INDEX IF EXISTS events_order; -- Prefix of events_order_topo_stream_room
DROP INDEX IF EXISTS events_topological_ordering; -- Prefix of events_order_topo_stream_room
DROP INDEX IF EXISTS events_stream_ordering; -- Duplicate of PRIMARY KEY
DROP INDEX IF EXISTS event_to_state_groups_id; -- Duplicate of PRIMARY KEY
DROP INDEX IF EXISTS event_push_actions_room_id_event_id_user_id_profile_tag; -- Duplicate of UNIQUE CONSTRAINT

DROP INDEX IF EXISTS st_extrem_id; -- Prefix of UNIQUE CONSTRAINT
DROP INDEX IF EXISTS event_signatures_id; -- Prefix of UNIQUE CONSTRAINT
DROP INDEX IF EXISTS redactions_event_id; -- Duplicate of UNIQUE CONSTRAINT

-- The following indices were unused
DROP INDEX IF EXISTS remote_media_cache_thumbnails_media_id;
DROP INDEX IF EXISTS evauth_edges_auth_id;
DROP INDEX IF EXISTS presence_stream_state;
