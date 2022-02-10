CREATE TABLE IF NOT EXISTS account_data(
    user_id TEXT NOT NULL,
    account_data_type TEXT NOT NULL, -- The type of the account_data.
    stream_id BIGINT NOT NULL, -- The version of the account_data.
    content TEXT NOT NULL,  -- The JSON content of the account_data
    CONSTRAINT account_data_uniqueness UNIQUE (user_id, account_data_type)
);


CREATE TABLE IF NOT EXISTS room_account_data(
    user_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    account_data_type TEXT NOT NULL, -- The type of the account_data.
    stream_id BIGINT NOT NULL, -- The version of the account_data.
    content TEXT NOT NULL,  -- The JSON content of the account_data
    CONSTRAINT room_account_data_uniqueness UNIQUE (user_id, room_id, account_data_type)
);


CREATE INDEX account_data_stream_id on account_data(user_id, stream_id);
CREATE INDEX room_account_data_stream_id on room_account_data(user_id, stream_id);
