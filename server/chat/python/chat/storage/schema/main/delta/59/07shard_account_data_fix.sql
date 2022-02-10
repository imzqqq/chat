-- We incorrectly populated these, so we delete them and let the
-- MultiWriterIdGenerator repopulate it.
DELETE FROM stream_positions WHERE stream_name = 'receipts' OR stream_name = 'account_data';
