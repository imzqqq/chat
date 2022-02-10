-- this didn't work on SQLite 3.7 (because of lack of partial indexes), so was
-- removed and replaced with 46/local_media_repository_url_idx.sql.
--
-- CREATE INDEX local_media_repository_url_idx ON local_media_repository(created_ts) WHERE url_cache IS NOT NULL;

-- we need to change `expires` to `expires_ts` so that we can index on it. SQLite doesn't support
-- indices on expressions until 3.9.
CREATE TABLE local_media_repository_url_cache_new(
    url TEXT,
    response_code INTEGER,
    etag TEXT,
    expires_ts BIGINT,
    og TEXT,
    media_id TEXT,
    download_ts BIGINT
);

INSERT INTO local_media_repository_url_cache_new
    SELECT url, response_code, etag, expires + download_ts, og, media_id, download_ts FROM local_media_repository_url_cache;

DROP TABLE local_media_repository_url_cache;
ALTER TABLE local_media_repository_url_cache_new RENAME TO local_media_repository_url_cache;

CREATE INDEX local_media_repository_url_cache_expires_idx ON local_media_repository_url_cache(expires_ts);
CREATE INDEX local_media_repository_url_cache_by_url_download_ts ON local_media_repository_url_cache(url, download_ts);
CREATE INDEX local_media_repository_url_cache_media_idx ON local_media_repository_url_cache(media_id);
