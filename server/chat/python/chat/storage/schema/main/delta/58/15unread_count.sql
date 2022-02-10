-- We're hijacking the push actions to store unread messages and unread counts (specified
-- in MSC2654) because doing otherwise would result in either performance issues or
-- reimplementing a consequent bit of the push actions.

-- Add columns to event_push_actions and event_push_actions_staging to track unread
-- messages and calculate unread counts.
ALTER TABLE event_push_actions_staging ADD COLUMN unread SMALLINT;
ALTER TABLE event_push_actions ADD COLUMN unread SMALLINT;

-- Add column to event_push_summary
ALTER TABLE event_push_summary ADD COLUMN unread_count BIGINT;