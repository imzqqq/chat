/** Using CREATE INDEX directly is deprecated in favour of using background
 * update see chat/storage/schema/delta/33/access_tokens_device_index.sql
 * and chat/storage/registration.py for an example using
 * "access_tokens_device_index" **/
 CREATE INDEX event_push_actions_stream_ordering on event_push_actions(
     stream_ordering, user_id
 );
