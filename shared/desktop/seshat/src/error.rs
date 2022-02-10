use thiserror::Error;

/// Result type for seshat operations.
pub type Result<T> = std::result::Result<T, Error>;

#[allow(clippy::upper_case_acronyms)]
#[derive(Error, Debug)]
/// Seshat error types.
pub enum Error {
    #[error("Sqlite pool error: {}", _0)]
    /// Error signaling that there was an error with the Sqlite connection
    /// pool.
    PoolError(#[from] r2d2::Error),
    #[error("Sqlite database error: {}", _0)]
    /// Error signaling that there was an error with a Sqlite transaction.
    DatabaseError(#[from] rusqlite::Error),
    #[error("Index error: {}", _0)]
    /// Error signaling that there was an error with the event indexer.
    IndexError(tantivy::TantivyError),
    #[error("File system error: {}", _0)]
    /// Error signaling that there was an error while reading from the
    /// filesystem.
    FsError(#[from] fs_extra::error::Error),
    #[error("IO error: {}", _0)]
    /// Error signaling that there was an error while doing a IO operation.
    IOError(#[from] std::io::Error),
    /// Error signaling that the database passphrase was incorrect.
    #[error("Error unlocking the database: {}", _0)]
    DatabaseUnlockError(String),
    /// Error when opening the Seshat database and reading the database version.
    #[error("Database version missmatch.")]
    DatabaseVersionError,
    /// Error when opening the Seshat database and reading the database version.
    #[error("Error opening the database: {}", _0)]
    DatabaseOpenError(String),
    /// Error signaling that sqlcipher support is missing.
    #[error("Sqlcipher error: {}", _0)]
    SqlCipherError(String),
    /// Error indicating that the index needs to be rebuilt.
    #[error("Error opening the database, the index needs to be rebuilt.")]
    ReindexError,
}

impl From<tantivy::TantivyError> for Error {
    fn from(err: tantivy::TantivyError) -> Self {
        Error::IndexError(err)
    }
}
