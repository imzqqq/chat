-- A noddy implementation of a distributed lock across workers. While a worker
-- has taken a lock out they should regularly update the `last_renewed_ts`
-- column, a lock will be considered dropped if `last_renewed_ts` is from ages
-- ago.
CREATE TABLE worker_locks (
    lock_name TEXT NOT NULL,
    lock_key TEXT NOT NULL,
    -- We write the instance name to ease manual debugging, we don't ever read
    -- from it.
    -- Note: instance names aren't guarenteed to be unique.
    instance_name TEXT NOT NULL,
    -- A random string generated each time an instance takes out a lock. Used by
    -- the instance to tell whether the lock is still held by it (e.g. in the
    -- case where the process stalls for a long time the lock may time out and
    -- be taken out by another instance, at which point the original instance
    -- can tell it no longer holds the lock as the tokens no longer match).
    token TEXT NOT NULL,
    last_renewed_ts BIGINT NOT NULL
);

CREATE UNIQUE INDEX worker_locks_key ON worker_locks (lock_name, lock_key);
