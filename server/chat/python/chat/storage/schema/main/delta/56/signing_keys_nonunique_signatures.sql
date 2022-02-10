/* The cross-signing signatures index should not be a unique index, because a
 * user may upload multiple signatures for the same target user. The previous
 * index was unique, so delete it if it's there and create a new non-unique
 * index. */

DROP INDEX IF EXISTS e2e_cross_signing_signatures_idx; CREATE INDEX IF NOT
EXISTS e2e_cross_signing_signatures2_idx ON e2e_cross_signing_signatures(user_id, target_user_id, target_device_id);
