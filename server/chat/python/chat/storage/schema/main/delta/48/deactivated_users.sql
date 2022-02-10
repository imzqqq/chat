/*
 * Store any accounts that have been requested to be deactivated.
 * We part the account from all the rooms its in when its
 * deactivated. This can take some time and chat may be restarted
 * before it completes, so store the user IDs here until the process
 * is complete.
 */
CREATE TABLE users_pending_deactivation (
    user_id TEXT NOT NULL
);
