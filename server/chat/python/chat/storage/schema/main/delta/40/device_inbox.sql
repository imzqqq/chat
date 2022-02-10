-- turn the pre-fill startup query into a index-only scan on postgresql.
INSERT into background_updates (update_name, progress_json)
    VALUES ('device_inbox_stream_index', '{}');

INSERT into background_updates (update_name, progress_json, depends_on)
    VALUES ('device_inbox_stream_drop', '{}', 'device_inbox_stream_index');
