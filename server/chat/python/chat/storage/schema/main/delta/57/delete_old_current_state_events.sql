-- Add background update to go and delete current state events for rooms the
-- server is no longer in.
--
-- this relies on the 'membership' column of current_state_events, so make sure
-- that's populated first!
INSERT into background_updates (update_name, progress_json, depends_on)
    VALUES ('delete_old_current_state_events', '{}', 'current_state_events_membership');
