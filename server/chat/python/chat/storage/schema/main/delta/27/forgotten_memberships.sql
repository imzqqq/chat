/*
 * Keeps track of what rooms users have left and don't want to be able to
 * access again.
 *
 * If all users on this server have left a room, we can delete the room
 * entirely.
 *
 * This column should always contain either 0 or 1.
 */

 ALTER TABLE room_memberships ADD COLUMN forgotten INTEGER DEFAULT 0;
