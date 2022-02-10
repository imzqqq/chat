CREATE TABLE IF NOT EXISTS registration_tokens(
    token TEXT NOT NULL,  -- The token that can be used for authentication.
    uses_allowed INT,  -- The total number of times this token can be used. NULL if no limit.
    pending INT NOT NULL, -- The number of in progress registrations using this token.
    completed INT NOT NULL, -- The number of times this token has been used to complete a registration.
    expiry_time BIGINT,  -- The latest time this token will be valid (epoch time in milliseconds). NULL if token doesn't expire.
    UNIQUE (token)
);
