-- Recalculate the stats for all rooms after the fix to joined_members erroneously
-- incrementing on per-room profile changes.

-- Note that the populate_stats_process_rooms background update is already set to
-- run if you're upgrading from Chat <1.0.0.

-- Additionally, if you've upgraded to v1.18.0 (which doesn't include this fix),
-- this bg job runs, and then update to v1.19.0, you'd end up with only half of
-- your rooms having room stats recalculated after this fix was in place.

-- So we've switched the old `populate_stats_process_rooms` background job to a
-- no-op, and then kick off a bg job with a new name, but with the same
-- functionality as the old one. This effectively restarts the background job
-- from the beginning, without running it twice in a row, supporting both
-- upgrade usecases.
INSERT INTO background_updates (ordering, update_name, progress_json) VALUES
    (5812, 'populate_stats_process_rooms_2', '{}');
