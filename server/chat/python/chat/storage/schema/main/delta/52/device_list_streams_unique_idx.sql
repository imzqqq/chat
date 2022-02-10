-- register a background update which will create a unique index on
-- device_lists_remote_cache
INSERT into background_updates (update_name, progress_json)
    VALUES ('device_lists_remote_cache_unique_idx', '{}');

-- and one on device_lists_remote_extremeties
INSERT into background_updates (update_name, progress_json, depends_on)
    VALUES (
        'device_lists_remote_extremeties_unique_idx', '{}',

        -- doesn't really depend on this, but we need to make sure both happen
        -- before we drop the old indexes.
        'device_lists_remote_cache_unique_idx'
    );

-- once they complete, we can drop the old indexes.
INSERT into background_updates (update_name, progress_json, depends_on)
    VALUES (
        'drop_device_list_streams_non_unique_indexes', '{}',
        'device_lists_remote_extremeties_unique_idx'
    );
