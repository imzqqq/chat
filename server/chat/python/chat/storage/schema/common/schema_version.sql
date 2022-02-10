CREATE TABLE IF NOT EXISTS schema_version(
    Lock CHAR(1) NOT NULL DEFAULT 'X' UNIQUE,  -- Makes sure this table only has one row.
    version INTEGER NOT NULL,
    upgraded BOOL NOT NULL,  -- Whether we reached this version from an upgrade or an initial schema.
    CHECK (Lock='X')
);

CREATE TABLE IF NOT EXISTS schema_compat_version(
    Lock CHAR(1) NOT NULL DEFAULT 'X' UNIQUE,  -- Makes sure this table only has one row.
    -- The SCHEMA_VERSION of the oldest chat this database can be used with
    compat_version INTEGER NOT NULL,
    CHECK (Lock='X')
);

CREATE TABLE IF NOT EXISTS applied_schema_deltas(
    version INTEGER NOT NULL,
    file TEXT NOT NULL,
    UNIQUE(version, file)
);

-- a list of schema files we have loaded on behalf of dynamic modules
CREATE TABLE IF NOT EXISTS applied_module_schemas(
    module_name TEXT NOT NULL,
    file TEXT NOT NULL,
    UNIQUE(module_name, file)
);
