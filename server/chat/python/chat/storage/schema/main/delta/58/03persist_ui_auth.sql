CREATE TABLE IF NOT EXISTS ui_auth_sessions(
    session_id TEXT NOT NULL,  -- The session ID passed to the client.
    creation_time BIGINT NOT NULL,  -- The time this session was created (epoch time in milliseconds).
    serverdict TEXT NOT NULL,  -- A JSON dictionary of arbitrary data added by Chat.
    clientdict TEXT NOT NULL,  -- A JSON dictionary of arbitrary data from the client.
    uri TEXT NOT NULL,  -- The URI the UI authentication session is using.
    method TEXT NOT NULL,  -- The HTTP method the UI authentication session is using.
    -- The clientdict, uri, and method make up an tuple that must be immutable
    -- throughout the lifetime of the UI Auth session.
    description TEXT NOT NULL,  -- A human readable description of the operation which caused the UI Auth flow to occur.
    UNIQUE (session_id)
);

CREATE TABLE IF NOT EXISTS ui_auth_sessions_credentials(
    session_id TEXT NOT NULL,  -- The corresponding UI Auth session.
    stage_type TEXT NOT NULL,  -- The stage type.
    result TEXT NOT NULL,  -- The result of the stage verification, stored as JSON.
    UNIQUE (session_id, stage_type),
    FOREIGN KEY (session_id)
        REFERENCES ui_auth_sessions (session_id)
);
