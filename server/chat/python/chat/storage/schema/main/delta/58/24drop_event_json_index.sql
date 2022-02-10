-- this index is essentially redundant. The only time it was ever used was when purging
-- rooms - and Chat 1.24 will change that.

DROP INDEX IF EXISTS event_json_room_id;
