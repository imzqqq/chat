use crate::{database::DatabaseGuard, ConduitResult, Error, Ruma};
use ruma::api::client::{
    error::ErrorKind,
    r0::backup::{
        add_backup_key_session, add_backup_key_sessions, add_backup_keys, create_backup,
        delete_backup, delete_backup_key_session, delete_backup_key_sessions, delete_backup_keys,
        get_backup, get_backup_key_session, get_backup_key_sessions, get_backup_keys,
        get_latest_backup, update_backup,
    },
};

#[cfg(feature = "conduit_bin")]
use rocket::{delete, get, post, put};

/// # `POST /chat/client/r0/room_keys/version`
///
/// Creates a new backup.
#[cfg_attr(
    feature = "conduit_bin",
    post("/chat/client/unstable/room_keys/version", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn create_backup_route(
    db: DatabaseGuard,
    body: Ruma<create_backup::Request>,
) -> ConduitResult<create_backup::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");
    let version = db
        .key_backups
        .create_backup(sender_user, &body.algorithm, &db.globals)?;

    db.flush()?;

    Ok(create_backup::Response { version }.into())
}

/// # `PUT /chat/client/r0/room_keys/version/{version}`
///
/// Update information about an existing backup. Only `auth_data` can be modified.
#[cfg_attr(
    feature = "conduit_bin",
    put("/chat/client/unstable/room_keys/version/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn update_backup_route(
    db: DatabaseGuard,
    body: Ruma<update_backup::Request<'_>>,
) -> ConduitResult<update_backup::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");
    db.key_backups
        .update_backup(sender_user, &body.version, &body.algorithm, &db.globals)?;

    db.flush()?;

    Ok(update_backup::Response {}.into())
}

/// # `GET /chat/client/r0/room_keys/version`
///
/// Get information about the latest backup version.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/unstable/room_keys/version", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_latest_backup_route(
    db: DatabaseGuard,
    body: Ruma<get_latest_backup::Request>,
) -> ConduitResult<get_latest_backup::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let (version, algorithm) =
        db.key_backups
            .get_latest_backup(sender_user)?
            .ok_or(Error::BadRequest(
                ErrorKind::NotFound,
                "Key backup does not exist.",
            ))?;

    Ok(get_latest_backup::Response {
        algorithm,
        count: (db.key_backups.count_keys(sender_user, &version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &version)?,
        version,
    }
    .into())
}

/// # `GET /chat/client/r0/room_keys/version`
///
/// Get information about an existing backup.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/unstable/room_keys/version/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_backup_route(
    db: DatabaseGuard,
    body: Ruma<get_backup::Request<'_>>,
) -> ConduitResult<get_backup::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");
    let algorithm = db
        .key_backups
        .get_backup(sender_user, &body.version)?
        .ok_or(Error::BadRequest(
            ErrorKind::NotFound,
            "Key backup does not exist.",
        ))?;

    Ok(get_backup::Response {
        algorithm,
        count: (db.key_backups.count_keys(sender_user, &body.version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &body.version)?,
        version: body.version.to_owned(),
    }
    .into())
}

/// # `DELETE /chat/client/r0/room_keys/version/{version}`
///
/// Delete an existing key backup.
///
/// - Deletes both information about the backup, as well as all key data related to the backup
#[cfg_attr(
    feature = "conduit_bin",
    delete("/chat/client/unstable/room_keys/version/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn delete_backup_route(
    db: DatabaseGuard,
    body: Ruma<delete_backup::Request<'_>>,
) -> ConduitResult<delete_backup::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    db.key_backups.delete_backup(sender_user, &body.version)?;

    db.flush()?;

    Ok(delete_backup::Response {}.into())
}

/// # `PUT /chat/client/r0/room_keys/keys`
///
/// Add the received backup keys to the database.
///
/// - Only manipulating the most recently created version of the backup is allowed
/// - Adds the keys to the backup
/// - Returns the new number of keys in this backup and the etag
#[cfg_attr(
    feature = "conduit_bin",
    put("/chat/client/unstable/room_keys/keys", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn add_backup_keys_route(
    db: DatabaseGuard,
    body: Ruma<add_backup_keys::Request<'_>>,
) -> ConduitResult<add_backup_keys::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    if Some(&body.version)
        != db
            .key_backups
            .get_latest_backup_version(sender_user)?
            .as_ref()
    {
        return Err(Error::BadRequest(
            ErrorKind::InvalidParam,
            "You may only manipulate the most recently created version of the backup.",
        ));
    }

    for (room_id, room) in &body.rooms {
        for (session_id, key_data) in &room.sessions {
            db.key_backups.add_key(
                sender_user,
                &body.version,
                room_id,
                session_id,
                key_data,
                &db.globals,
            )?
        }
    }

    db.flush()?;

    Ok(add_backup_keys::Response {
        count: (db.key_backups.count_keys(sender_user, &body.version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &body.version)?,
    }
    .into())
}

/// # `PUT /chat/client/r0/room_keys/keys/{roomId}`
///
/// Add the received backup keys to the database.
///
/// - Only manipulating the most recently created version of the backup is allowed
/// - Adds the keys to the backup
/// - Returns the new number of keys in this backup and the etag
#[cfg_attr(
    feature = "conduit_bin",
    put("/chat/client/unstable/room_keys/keys/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn add_backup_key_sessions_route(
    db: DatabaseGuard,
    body: Ruma<add_backup_key_sessions::Request<'_>>,
) -> ConduitResult<add_backup_key_sessions::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    if Some(&body.version)
        != db
            .key_backups
            .get_latest_backup_version(sender_user)?
            .as_ref()
    {
        return Err(Error::BadRequest(
            ErrorKind::InvalidParam,
            "You may only manipulate the most recently created version of the backup.",
        ));
    }

    for (session_id, key_data) in &body.sessions {
        db.key_backups.add_key(
            sender_user,
            &body.version,
            &body.room_id,
            session_id,
            key_data,
            &db.globals,
        )?
    }

    db.flush()?;

    Ok(add_backup_key_sessions::Response {
        count: (db.key_backups.count_keys(sender_user, &body.version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &body.version)?,
    }
    .into())
}

/// # `PUT /chat/client/r0/room_keys/keys/{roomId}/{sessionId}`
///
/// Add the received backup key to the database.
///
/// - Only manipulating the most recently created version of the backup is allowed
/// - Adds the keys to the backup
/// - Returns the new number of keys in this backup and the etag
#[cfg_attr(
    feature = "conduit_bin",
    put("/chat/client/unstable/room_keys/keys/<_>/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn add_backup_key_session_route(
    db: DatabaseGuard,
    body: Ruma<add_backup_key_session::Request<'_>>,
) -> ConduitResult<add_backup_key_session::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    if Some(&body.version)
        != db
            .key_backups
            .get_latest_backup_version(sender_user)?
            .as_ref()
    {
        return Err(Error::BadRequest(
            ErrorKind::InvalidParam,
            "You may only manipulate the most recently created version of the backup.",
        ));
    }

    db.key_backups.add_key(
        sender_user,
        &body.version,
        &body.room_id,
        &body.session_id,
        &body.session_data,
        &db.globals,
    )?;

    db.flush()?;

    Ok(add_backup_key_session::Response {
        count: (db.key_backups.count_keys(sender_user, &body.version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &body.version)?,
    }
    .into())
}

/// # `GET /chat/client/r0/room_keys/keys`
///
/// Retrieves all keys from the backup.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/unstable/room_keys/keys", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_backup_keys_route(
    db: DatabaseGuard,
    body: Ruma<get_backup_keys::Request<'_>>,
) -> ConduitResult<get_backup_keys::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let rooms = db.key_backups.get_all(sender_user, &body.version)?;

    Ok(get_backup_keys::Response { rooms }.into())
}

/// # `GET /chat/client/r0/room_keys/keys/{roomId}`
///
/// Retrieves all keys from the backup for a given room.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/unstable/room_keys/keys/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_backup_key_sessions_route(
    db: DatabaseGuard,
    body: Ruma<get_backup_key_sessions::Request<'_>>,
) -> ConduitResult<get_backup_key_sessions::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let sessions = db
        .key_backups
        .get_room(sender_user, &body.version, &body.room_id)?;

    Ok(get_backup_key_sessions::Response { sessions }.into())
}

/// # `GET /chat/client/r0/room_keys/keys/{roomId}/{sessionId}`
///
/// Retrieves a key from the backup.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/unstable/room_keys/keys/<_>/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_backup_key_session_route(
    db: DatabaseGuard,
    body: Ruma<get_backup_key_session::Request<'_>>,
) -> ConduitResult<get_backup_key_session::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let key_data = db
        .key_backups
        .get_session(sender_user, &body.version, &body.room_id, &body.session_id)?
        .ok_or(Error::BadRequest(
            ErrorKind::NotFound,
            "Backup key not found for this user's session.",
        ))?;

    Ok(get_backup_key_session::Response { key_data }.into())
}

/// # `DELETE /chat/client/r0/room_keys/keys`
///
/// Delete the keys from the backup.
#[cfg_attr(
    feature = "conduit_bin",
    delete("/chat/client/unstable/room_keys/keys", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn delete_backup_keys_route(
    db: DatabaseGuard,
    body: Ruma<delete_backup_keys::Request<'_>>,
) -> ConduitResult<delete_backup_keys::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    db.key_backups.delete_all_keys(sender_user, &body.version)?;

    db.flush()?;

    Ok(delete_backup_keys::Response {
        count: (db.key_backups.count_keys(sender_user, &body.version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &body.version)?,
    }
    .into())
}

/// # `DELETE /chat/client/r0/room_keys/keys/{roomId}`
///
/// Delete the keys from the backup for a given room.
#[cfg_attr(
    feature = "conduit_bin",
    delete("/chat/client/unstable/room_keys/keys/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn delete_backup_key_sessions_route(
    db: DatabaseGuard,
    body: Ruma<delete_backup_key_sessions::Request<'_>>,
) -> ConduitResult<delete_backup_key_sessions::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    db.key_backups
        .delete_room_keys(sender_user, &body.version, &body.room_id)?;

    db.flush()?;

    Ok(delete_backup_key_sessions::Response {
        count: (db.key_backups.count_keys(sender_user, &body.version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &body.version)?,
    }
    .into())
}

/// # `DELETE /chat/client/r0/room_keys/keys/{roomId}/{sessionId}`
///
/// Delete a key from the backup.
#[cfg_attr(
    feature = "conduit_bin",
    delete("/chat/client/unstable/room_keys/keys/<_>/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn delete_backup_key_session_route(
    db: DatabaseGuard,
    body: Ruma<delete_backup_key_session::Request<'_>>,
) -> ConduitResult<delete_backup_key_session::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    db.key_backups
        .delete_room_key(sender_user, &body.version, &body.room_id, &body.session_id)?;

    db.flush()?;

    Ok(delete_backup_key_session::Response {
        count: (db.key_backups.count_keys(sender_user, &body.version)? as u32).into(),
        etag: db.key_backups.get_etag(sender_user, &body.version)?,
    }
    .into())
}
