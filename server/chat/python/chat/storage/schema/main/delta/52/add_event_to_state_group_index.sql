-- This is needed to efficiently check for unreferenced state groups during
-- purge. Added events_to_state_group(state_group) index
INSERT into background_updates (update_name, progress_json)
    VALUES ('event_to_state_groups_sg_index', '{}');
