CREATE TABLE IF NOT EXISTS event_push_actions(
    room_id TEXT NOT NULL,
    event_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    profile_tag VARCHAR(32),
    actions TEXT NOT NULL,
    CONSTRAINT event_id_user_id_profile_tag_uniqueness UNIQUE (room_id, event_id, user_id, profile_tag)
);


CREATE INDEX event_push_actions_room_id_event_id_user_id_profile_tag on event_push_actions(room_id, event_id, user_id, profile_tag);
CREATE INDEX event_push_actions_room_id_user_id on event_push_actions(room_id, user_id);
