-- A table of the IP address and user-agent used to complete each step of a
-- user-interactive authentication session.
CREATE TABLE IF NOT EXISTS ui_auth_sessions_ips(
    session_id TEXT NOT NULL,
    ip TEXT NOT NULL,
    user_agent TEXT NOT NULL,
    UNIQUE (session_id, ip, user_agent),
    FOREIGN KEY (session_id)
        REFERENCES ui_auth_sessions (session_id)
);
