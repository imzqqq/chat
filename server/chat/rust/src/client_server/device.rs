use crate::{database::DatabaseGuard, utils, ConduitResult, Error, Ruma};
use ruma::api::client::{
    error::ErrorKind,
    r0::{
        device::{self, delete_device, delete_devices, get_device, get_devices, update_device},
        uiaa::{AuthFlow, AuthType, UiaaInfo},
    },
};

use super::SESSION_ID_LENGTH;
#[cfg(feature = "conduit_bin")]
use rocket::{delete, get, post, put};

/// # `GET /chat/client/r0/devices`
///
/// Get metadata on all devices of the sender user.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/r0/devices", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_devices_route(
    db: DatabaseGuard,
    body: Ruma<get_devices::Request>,
) -> ConduitResult<get_devices::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let devices: Vec<device::Device> = db
        .users
        .all_devices_metadata(sender_user)
        .filter_map(|r| r.ok()) // Filter out buggy devices
        .collect();

    Ok(get_devices::Response { devices }.into())
}

/// # `GET /chat/client/r0/devices/{deviceId}`
///
/// Get metadata on a single device of the sender user.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/r0/devices/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_device_route(
    db: DatabaseGuard,
    body: Ruma<get_device::Request<'_>>,
) -> ConduitResult<get_device::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let device = db
        .users
        .get_device_metadata(sender_user, &body.body.device_id)?
        .ok_or(Error::BadRequest(ErrorKind::NotFound, "Device not found."))?;

    Ok(get_device::Response { device }.into())
}

/// # `PUT /chat/client/r0/devices/{deviceId}`
///
/// Updates the metadata on a given device of the sender user.
#[cfg_attr(
    feature = "conduit_bin",
    put("/chat/client/r0/devices/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn update_device_route(
    db: DatabaseGuard,
    body: Ruma<update_device::Request<'_>>,
) -> ConduitResult<update_device::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let mut device = db
        .users
        .get_device_metadata(sender_user, &body.device_id)?
        .ok_or(Error::BadRequest(ErrorKind::NotFound, "Device not found."))?;

    device.display_name = body.display_name.clone();

    db.users
        .update_device_metadata(sender_user, &body.device_id, &device)?;

    db.flush()?;

    Ok(update_device::Response {}.into())
}

/// # `PUT /chat/client/r0/devices/{deviceId}`
///
/// Deletes the given device.
///
/// - Requires UIAA to verify user password
/// - Invalidates access token
/// - Deletes device metadata (device id, device display name, last seen ip, last seen ts)
/// - Forgets to-device events
/// - Triggers device list updates
#[cfg_attr(
    feature = "conduit_bin",
    delete("/chat/client/r0/devices/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn delete_device_route(
    db: DatabaseGuard,
    body: Ruma<delete_device::Request<'_>>,
) -> ConduitResult<delete_device::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");
    let sender_device = body.sender_device.as_ref().expect("user is authenticated");

    // UIAA
    let mut uiaainfo = UiaaInfo {
        flows: vec![AuthFlow {
            stages: vec![AuthType::Password],
        }],
        completed: Vec::new(),
        params: Default::default(),
        session: None,
        auth_error: None,
    };

    if let Some(auth) = &body.auth {
        let (worked, uiaainfo) = db.uiaa.try_auth(
            sender_user,
            sender_device,
            auth,
            &uiaainfo,
            &db.users,
            &db.globals,
        )?;
        if !worked {
            return Err(Error::Uiaa(uiaainfo));
        }
    // Success!
    } else if let Some(json) = body.json_body {
        uiaainfo.session = Some(utils::random_string(SESSION_ID_LENGTH));
        db.uiaa
            .create(sender_user, sender_device, &uiaainfo, &json)?;
        return Err(Error::Uiaa(uiaainfo));
    } else {
        return Err(Error::BadRequest(ErrorKind::NotJson, "Not json."));
    }

    db.users.remove_device(sender_user, &body.device_id)?;

    db.flush()?;

    Ok(delete_device::Response {}.into())
}

/// # `PUT /chat/client/r0/devices/{deviceId}`
///
/// Deletes the given device.
///
/// - Requires UIAA to verify user password
///
/// For each device:
/// - Invalidates access token
/// - Deletes device metadata (device id, device display name, last seen ip, last seen ts)
/// - Forgets to-device events
/// - Triggers device list updates
#[cfg_attr(
    feature = "conduit_bin",
    post("/chat/client/r0/delete_devices", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn delete_devices_route(
    db: DatabaseGuard,
    body: Ruma<delete_devices::Request<'_>>,
) -> ConduitResult<delete_devices::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");
    let sender_device = body.sender_device.as_ref().expect("user is authenticated");

    // UIAA
    let mut uiaainfo = UiaaInfo {
        flows: vec![AuthFlow {
            stages: vec![AuthType::Password],
        }],
        completed: Vec::new(),
        params: Default::default(),
        session: None,
        auth_error: None,
    };

    if let Some(auth) = &body.auth {
        let (worked, uiaainfo) = db.uiaa.try_auth(
            sender_user,
            sender_device,
            auth,
            &uiaainfo,
            &db.users,
            &db.globals,
        )?;
        if !worked {
            return Err(Error::Uiaa(uiaainfo));
        }
    // Success!
    } else if let Some(json) = body.json_body {
        uiaainfo.session = Some(utils::random_string(SESSION_ID_LENGTH));
        db.uiaa
            .create(sender_user, sender_device, &uiaainfo, &json)?;
        return Err(Error::Uiaa(uiaainfo));
    } else {
        return Err(Error::BadRequest(ErrorKind::NotJson, "Not json."));
    }

    for device_id in &body.devices {
        db.users.remove_device(sender_user, device_id)?
    }

    db.flush()?;

    Ok(delete_devices::Response {}.into())
}
