/* We used to create a table called current_state_resets, but this is no
 * longer used and is removed in delta 54.
 */

/* The outlier events that have aquired a state group typically through
 * backfill. This is tracked separately to the events table, as assigning a
 * state group change the position of the existing event in the stream
 * ordering.
 * However since a stream_ordering is assigned in persist_event for the
 * (event, state) pair, we can use that stream_ordering to identify when
 * the new state was assigned for the event.
 */
CREATE TABLE IF NOT EXISTS ex_outlier_stream(
    event_stream_ordering BIGINT PRIMARY KEY NOT NULL,
    event_id TEXT NOT NULL,
    state_group BIGINT NOT NULL
);
