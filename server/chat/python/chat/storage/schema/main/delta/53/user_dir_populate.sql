-- Set up staging tables
INSERT INTO background_updates (update_name, progress_json) VALUES
    ('populate_user_directory_createtables', '{}');

-- Run through each room and update the user directory according to who is in it
INSERT INTO background_updates (update_name, progress_json, depends_on) VALUES
    ('populate_user_directory_process_rooms', '{}', 'populate_user_directory_createtables');

-- Insert all users, if search_all_users is on
INSERT INTO background_updates (update_name, progress_json, depends_on) VALUES
    ('populate_user_directory_process_users', '{}', 'populate_user_directory_process_rooms');

-- Clean up staging tables
INSERT INTO background_updates (update_name, progress_json, depends_on) VALUES
    ('populate_user_directory_cleanup', '{}', 'populate_user_directory_process_users');
