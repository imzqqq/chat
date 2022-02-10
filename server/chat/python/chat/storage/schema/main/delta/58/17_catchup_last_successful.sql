-- This column tracks the stream_ordering of the event that was most recently
-- successfully transmitted to the destination.
-- A value of NULL means that we have not sent an event successfully yet
-- (at least, not since the introduction of this column).
ALTER TABLE destinations
    ADD COLUMN last_successful_stream_ordering BIGINT;
