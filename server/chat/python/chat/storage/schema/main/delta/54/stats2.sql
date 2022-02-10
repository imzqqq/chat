-- This delta file gets run after `54/stats.sql` delta.

-- We want to add some indices to the temporary stats table, so we re-insert
-- 'populate_stats_createtables' if we are still processing the rooms update.
INSERT INTO background_updates (update_name, progress_json)
    SELECT 'populate_stats_createtables', '{}'
    WHERE
        'populate_stats_process_rooms' IN (
            SELECT update_name FROM background_updates
        )
        AND 'populate_stats_createtables' NOT IN (  -- don't insert if already exists
            SELECT update_name FROM background_updates
        );
