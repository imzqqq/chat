/*
 * a table which records mappings from external auth providers to mxids
 */
CREATE TABLE IF NOT EXISTS user_external_ids (
    auth_provider TEXT NOT NULL,
    external_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    UNIQUE (auth_provider, external_id)
);
