-- Add a column to specify which instance wrote the row. Historic rows have
-- `NULL`, which indicates that the master instance wrote them.
ALTER TABLE presence_stream ADD COLUMN instance_name TEXT;
