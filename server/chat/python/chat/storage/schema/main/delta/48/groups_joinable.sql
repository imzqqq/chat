/* 
 * This isn't a real ENUM because sqlite doesn't support it
 * and we use a default of NULL for inserted rows and interpret
 * NULL at the python store level as necessary so that existing
 * rows are given the correct default policy.
 */
ALTER TABLE groups ADD COLUMN join_policy TEXT NOT NULL DEFAULT 'invite';
