ALTER TABLE room_account_data ADD COLUMN instance_name TEXT;
ALTER TABLE room_tags_revisions ADD COLUMN instance_name TEXT;
ALTER TABLE account_data ADD COLUMN instance_name TEXT;

ALTER TABLE receipts_linearized ADD COLUMN instance_name TEXT;
