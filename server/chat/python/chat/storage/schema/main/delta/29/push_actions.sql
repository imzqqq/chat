ALTER TABLE event_push_actions ADD COLUMN topological_ordering BIGINT;
ALTER TABLE event_push_actions ADD COLUMN stream_ordering BIGINT;
ALTER TABLE event_push_actions ADD COLUMN notif SMALLINT;
ALTER TABLE event_push_actions ADD COLUMN highlight SMALLINT;

UPDATE event_push_actions SET stream_ordering = (
    SELECT stream_ordering FROM events WHERE event_id = event_push_actions.event_id
), topological_ordering = (
    SELECT topological_ordering FROM events WHERE event_id = event_push_actions.event_id
);

UPDATE event_push_actions SET notif = 1, highlight = 0;

/** Using CREATE INDEX directly is deprecated in favour of using background
 * update see chat/storage/schema/delta/33/access_tokens_device_index.sql
 * and chat/storage/registration.py for an example using
 * "access_tokens_device_index" **/
CREATE INDEX event_push_actions_rm_tokens on event_push_actions(
    user_id, room_id, topological_ordering, stream_ordering
);
