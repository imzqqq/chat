-- a previous version of the "devices_for_e2e_keys" delta set all the device
-- names to "unknown device". This wasn't terribly helpful
UPDATE devices
    SET display_name = NULL
    WHERE display_name = 'unknown device';
