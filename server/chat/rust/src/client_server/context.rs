use crate::{database::DatabaseGuard, ConduitResult, Error, Ruma};
use ruma::api::client::{error::ErrorKind, r0::context::get_context};
use std::convert::TryFrom;

#[cfg(feature = "conduit_bin")]
use rocket::get;

/// # `GET /chat/client/r0/rooms/{roomId}/context`
///
/// Allows loading room history around an event.
///
/// - Only works if the user is joined (TODO: always allow, but only show events if the user was
/// joined, depending on history_visibility)
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/r0/rooms/<_>/context/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_context_route(
    db: DatabaseGuard,
    body: Ruma<get_context::Request<'_>>,
) -> ConduitResult<get_context::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    if !db.rooms.is_joined(sender_user, &body.room_id)? {
        return Err(Error::BadRequest(
            ErrorKind::Forbidden,
            "You don't have permission to view this room.",
        ));
    }

    let base_pdu_id = db
        .rooms
        .get_pdu_id(&body.event_id)?
        .ok_or(Error::BadRequest(
            ErrorKind::NotFound,
            "Base event id not found.",
        ))?;

    let base_token = db.rooms.pdu_count(&base_pdu_id)?;

    let base_event = db
        .rooms
        .get_pdu_from_id(&base_pdu_id)?
        .ok_or(Error::BadRequest(
            ErrorKind::NotFound,
            "Base event not found.",
        ))?
        .to_room_event();

    let events_before: Vec<_> = db
        .rooms
        .pdus_until(sender_user, &body.room_id, base_token)?
        .take(
            u32::try_from(body.limit).map_err(|_| {
                Error::BadRequest(ErrorKind::InvalidParam, "Limit value is invalid.")
            })? as usize
                / 2,
        )
        .filter_map(|r| r.ok()) // Remove buggy events
        .collect();

    let start_token = events_before
        .last()
        .and_then(|(pdu_id, _)| db.rooms.pdu_count(pdu_id).ok())
        .map(|count| count.to_string());

    let events_before: Vec<_> = events_before
        .into_iter()
        .map(|(_, pdu)| pdu.to_room_event())
        .collect();

    let events_after: Vec<_> = db
        .rooms
        .pdus_after(sender_user, &body.room_id, base_token)?
        .take(
            u32::try_from(body.limit).map_err(|_| {
                Error::BadRequest(ErrorKind::InvalidParam, "Limit value is invalid.")
            })? as usize
                / 2,
        )
        .filter_map(|r| r.ok()) // Remove buggy events
        .collect();

    let end_token = events_after
        .last()
        .and_then(|(pdu_id, _)| db.rooms.pdu_count(pdu_id).ok())
        .map(|count| count.to_string());

    let events_after: Vec<_> = events_after
        .into_iter()
        .map(|(_, pdu)| pdu.to_room_event())
        .collect();

    let mut resp = get_context::Response::new();
    resp.start = start_token;
    resp.end = end_token;
    resp.events_before = events_before;
    resp.event = Some(base_event);
    resp.events_after = events_after;
    resp.state = db // TODO: State at event
        .rooms
        .room_state_full(&body.room_id)?
        .values()
        .map(|pdu| pdu.to_state_event())
        .collect();

    Ok(resp.into())
}
