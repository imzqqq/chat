/* The type of the user: NULL for a regular user, or one of the constants in 
 * chat.api.constants.UserTypes
 */
ALTER TABLE users ADD COLUMN user_type TEXT DEFAULT NULL;
