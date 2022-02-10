/** Using CREATE INDEX directly is deprecated in favour of using background
 * update see chat/storage/schema/delta/33/access_tokens_device_index.sql
 * and chat/storage/registration.py for an example using
 * "access_tokens_device_index" **/
CREATE INDEX events_room_stream on events(room_id, stream_ordering);
