-- change the user_directory table to also cover global local user profiles
-- rather than just profiles within specific rooms.

CREATE TABLE user_directory2 (
    user_id TEXT NOT NULL,
    room_id TEXT,
    display_name TEXT,
    avatar_url TEXT
);

INSERT INTO user_directory2(user_id, room_id, display_name, avatar_url)
    SELECT user_id, room_id, display_name, avatar_url from user_directory;

DROP TABLE user_directory;
ALTER TABLE user_directory2 RENAME TO user_directory;

-- create indexes after doing the inserts because that's more efficient.
-- it also means we can give it the same name as the old one without renaming.
CREATE INDEX user_directory_room_idx ON user_directory(room_id);
CREATE UNIQUE INDEX user_directory_user_idx ON user_directory(user_id);
