-- we no longer keep sent outbound device pokes in the db; clear them out
-- so that we don't have to worry about them.
--
-- This is a sequence scan, but it doesn't take too long.

DELETE FROM device_lists_outbound_pokes WHERE sent;
