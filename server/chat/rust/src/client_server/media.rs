use crate::{
    database::{media::FileMeta, DatabaseGuard},
    utils, ConduitResult, Error, Ruma,
};
use ruma::api::client::{
    error::ErrorKind,
    r0::media::{create_content, get_content, get_content_thumbnail, get_media_config},
};
use std::convert::TryInto;

#[cfg(feature = "conduit_bin")]
use rocket::{get, post};

const MXC_LENGTH: usize = 32;

/// # `GET /chat/media/r0/config`
///
/// Returns max upload size.
#[cfg_attr(feature = "conduit_bin", get("/chat/media/r0/config"))]
#[tracing::instrument(skip(db))]
pub async fn get_media_config_route(
    db: DatabaseGuard,
) -> ConduitResult<get_media_config::Response> {
    Ok(get_media_config::Response {
        upload_size: db.globals.max_request_size().into(),
    }
    .into())
}

/// # `POST /chat/media/r0/upload`
///
/// Permanently save media in the server.
///
/// - Some metadata will be saved in the database
/// - Media will be saved in the media/ directory
#[cfg_attr(
    feature = "conduit_bin",
    post("/chat/media/r0/upload", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn create_content_route(
    db: DatabaseGuard,
    body: Ruma<create_content::Request<'_>>,
) -> ConduitResult<create_content::Response> {
    let mxc = format!(
        "mxc://{}/{}",
        db.globals.server_name(),
        utils::random_string(MXC_LENGTH)
    );

    db.media
        .create(
            mxc.clone(),
            &db.globals,
            &body
                .filename
                .as_ref()
                .map(|filename| "inline; filename=".to_owned() + filename)
                .as_deref(),
            &body.content_type.as_deref(),
            &body.file,
        )
        .await?;

    db.flush()?;

    Ok(create_content::Response {
        content_uri: mxc.try_into().expect("Invalid mxc:// URI"),
        blurhash: None,
    }
    .into())
}

/// # `POST /chat/media/r0/download/{serverName}/{mediaId}`
///
/// Load media from our server or over federation.
///
/// - Only allows federation if `allow_remote` is true
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/media/r0/download/<_>/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_content_route(
    db: DatabaseGuard,
    body: Ruma<get_content::Request<'_>>,
) -> ConduitResult<get_content::Response> {
    let mxc = format!("mxc://{}/{}", body.server_name, body.media_id);

    if let Some(FileMeta {
        content_disposition,
        content_type,
        file,
    }) = db.media.get(&db.globals, &mxc).await?
    {
        Ok(get_content::Response {
            file,
            content_type,
            content_disposition,
        }
        .into())
    } else if &*body.server_name != db.globals.server_name() && body.allow_remote {
        let get_content_response = db
            .sending
            .send_federation_request(
                &db.globals,
                &body.server_name,
                get_content::Request {
                    allow_remote: false,
                    server_name: &body.server_name,
                    media_id: &body.media_id,
                },
            )
            .await?;

        db.media
            .create(
                mxc,
                &db.globals,
                &get_content_response.content_disposition.as_deref(),
                &get_content_response.content_type.as_deref(),
                &get_content_response.file,
            )
            .await?;

        Ok(get_content_response.into())
    } else {
        Err(Error::BadRequest(ErrorKind::NotFound, "Media not found."))
    }
}

/// # `POST /chat/media/r0/thumbnail/{serverName}/{mediaId}`
///
/// Load media thumbnail from our server or over federation.
///
/// - Only allows federation if `allow_remote` is true
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/media/r0/thumbnail/<_>/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_content_thumbnail_route(
    db: DatabaseGuard,
    body: Ruma<get_content_thumbnail::Request<'_>>,
) -> ConduitResult<get_content_thumbnail::Response> {
    let mxc = format!("mxc://{}/{}", body.server_name, body.media_id);

    if let Some(FileMeta {
        content_type, file, ..
    }) = db
        .media
        .get_thumbnail(
            mxc.clone(),
            &db.globals,
            body.width
                .try_into()
                .map_err(|_| Error::BadRequest(ErrorKind::InvalidParam, "Width is invalid."))?,
            body.height
                .try_into()
                .map_err(|_| Error::BadRequest(ErrorKind::InvalidParam, "Width is invalid."))?,
        )
        .await?
    {
        Ok(get_content_thumbnail::Response { file, content_type }.into())
    } else if &*body.server_name != db.globals.server_name() && body.allow_remote {
        let get_thumbnail_response = db
            .sending
            .send_federation_request(
                &db.globals,
                &body.server_name,
                get_content_thumbnail::Request {
                    allow_remote: false,
                    height: body.height,
                    width: body.width,
                    method: body.method.clone(),
                    server_name: &body.server_name,
                    media_id: &body.media_id,
                },
            )
            .await?;

        db.media
            .upload_thumbnail(
                mxc,
                &db.globals,
                &None,
                &get_thumbnail_response.content_type,
                body.width.try_into().expect("all UInts are valid u32s"),
                body.height.try_into().expect("all UInts are valid u32s"),
                &get_thumbnail_response.file,
            )
            .await?;

        Ok(get_thumbnail_response.into())
    } else {
        Err(Error::BadRequest(ErrorKind::NotFound, "Media not found."))
    }
}
