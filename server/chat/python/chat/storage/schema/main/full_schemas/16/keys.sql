-- we used to create a table called server_tls_certificates, but this is no
-- longer used, and is removed in delta 54.

CREATE TABLE IF NOT EXISTS server_signature_keys(
  server_name TEXT, -- Server name.
  key_id TEXT, -- Key version.
  from_server TEXT, -- Which key server the key was fetched form.
  ts_added_ms BIGINT, -- When the key was added.
  verify_key bytea, -- NACL verification key.
  UNIQUE (server_name, key_id)
);
