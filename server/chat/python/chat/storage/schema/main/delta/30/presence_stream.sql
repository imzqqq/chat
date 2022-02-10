 CREATE TABLE presence_stream(
     stream_id BIGINT,
     user_id TEXT,
     state TEXT,
     last_active_ts BIGINT,
     last_federation_update_ts BIGINT,
     last_user_sync_ts BIGINT,
     status_msg TEXT,
     currently_active BOOLEAN
 );

 CREATE INDEX presence_stream_id ON presence_stream(stream_id, user_id);
 CREATE INDEX presence_stream_user_id ON presence_stream(user_id);
 CREATE INDEX presence_stream_state ON presence_stream(state);
