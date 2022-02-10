-- Stores guest account access tokens generated for unbound 3pids.
CREATE TABLE threepid_guest_access_tokens(
    medium TEXT, -- The medium of the 3pid. Must be "email".
    address TEXT, -- The 3pid address.
    guest_access_token TEXT, -- The access token for a guest user for this 3pid.
    first_inviter TEXT -- User ID of the first user to invite this 3pid to a room.
);

CREATE UNIQUE INDEX threepid_guest_access_tokens_index ON threepid_guest_access_tokens(medium, address);
