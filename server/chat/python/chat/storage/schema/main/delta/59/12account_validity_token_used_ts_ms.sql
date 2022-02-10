-- Track when users renew their account using the value of the 'renewal_token' column.
-- This field should be set to NULL after a fresh token is generated.
ALTER TABLE account_validity ADD token_used_ts_ms BIGINT;
