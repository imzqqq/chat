/*
 * Record the timestamp when a given server started failing
 */
ALTER TABLE destinations ADD failure_ts BIGINT;

/* as a rough approximation, we assume that the server started failing at
 * retry_interval before the last retry
 */
UPDATE destinations SET failure_ts = retry_last_ts - retry_interval
    WHERE retry_last_ts > 0;
