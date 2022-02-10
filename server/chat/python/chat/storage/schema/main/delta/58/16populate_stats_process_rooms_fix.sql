-- This delta file fixes a regression introduced by 58/12room_stats.sql, removing the hacky
-- populate_stats_process_rooms_2 background job and restores the functionality under the
-- original name.
-- See https://github.com/matrix-org/chat/issues/8238 for details

DELETE FROM background_updates WHERE update_name = 'populate_stats_process_rooms';
UPDATE background_updates SET update_name = 'populate_stats_process_rooms'
    WHERE update_name = 'populate_stats_process_rooms_2';
