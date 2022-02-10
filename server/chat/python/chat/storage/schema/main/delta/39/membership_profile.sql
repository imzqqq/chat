ALTER TABLE room_memberships ADD COLUMN display_name TEXT;
ALTER TABLE room_memberships ADD COLUMN avatar_url TEXT;

INSERT into background_updates (update_name, progress_json)
    VALUES ('room_membership_profile_update', '{}');
