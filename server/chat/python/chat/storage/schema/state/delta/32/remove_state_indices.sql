-- The following indices are redundant, other indices are equivalent or
-- supersets
DROP INDEX IF EXISTS state_groups_id; -- Duplicate of PRIMARY KEY
