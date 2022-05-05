use crate::{database::DatabaseGuard, ConduitResult, Ruma};
use ruma::{
    api::client::r0::tag::{create_tag, delete_tag, get_tags},
    events::{
        tag::{TagEvent, TagEventContent},
        EventType,
    },
};
use std::collections::BTreeMap;

#[cfg(feature = "conduit_bin")]
use rocket::{delete, get, put};

/// # `PUT /chat/client/r0/user/{userId}/rooms/{roomId}/tags/{tag}`
///
/// Adds a tag to the room.
///
/// - Inserts the tag into the tag event of the room account data.
#[cfg_attr(
    feature = "conduit_bin",
    put("/chat/client/r0/user/<_>/rooms/<_>/tags/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn update_tag_route(
    db: DatabaseGuard,
    body: Ruma<create_tag::Request<'_>>,
) -> ConduitResult<create_tag::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let mut tags_event = db
        .account_data
        .get(Some(&body.room_id), sender_user, EventType::Tag)?
        .unwrap_or_else(|| TagEvent {
            content: TagEventContent {
                tags: BTreeMap::new(),
            },
        });
    tags_event
        .content
        .tags
        .insert(body.tag.clone().into(), body.tag_info.clone());

    db.account_data.update(
        Some(&body.room_id),
        sender_user,
        EventType::Tag,
        &tags_event,
        &db.globals,
    )?;

    db.flush()?;

    Ok(create_tag::Response {}.into())
}

/// # `DELETE /chat/client/r0/user/{userId}/rooms/{roomId}/tags/{tag}`
///
/// Deletes a tag from the room.
///
/// - Removes the tag from the tag event of the room account data.
#[cfg_attr(
    feature = "conduit_bin",
    delete("/chat/client/r0/user/<_>/rooms/<_>/tags/<_>", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn delete_tag_route(
    db: DatabaseGuard,
    body: Ruma<delete_tag::Request<'_>>,
) -> ConduitResult<delete_tag::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    let mut tags_event = db
        .account_data
        .get(Some(&body.room_id), sender_user, EventType::Tag)?
        .unwrap_or_else(|| TagEvent {
            content: TagEventContent {
                tags: BTreeMap::new(),
            },
        });
    tags_event.content.tags.remove(&body.tag.clone().into());

    db.account_data.update(
        Some(&body.room_id),
        sender_user,
        EventType::Tag,
        &tags_event,
        &db.globals,
    )?;

    db.flush()?;

    Ok(delete_tag::Response {}.into())
}

/// # `GET /chat/client/r0/user/{userId}/rooms/{roomId}/tags`
///
/// Returns tags on the room.
///
/// - Gets the tag event of the room account data.
#[cfg_attr(
    feature = "conduit_bin",
    get("/chat/client/r0/user/<_>/rooms/<_>/tags", data = "<body>")
)]
#[tracing::instrument(skip(db, body))]
pub async fn get_tags_route(
    db: DatabaseGuard,
    body: Ruma<get_tags::Request<'_>>,
) -> ConduitResult<get_tags::Response> {
    let sender_user = body.sender_user.as_ref().expect("user is authenticated");

    Ok(get_tags::Response {
        tags: db
            .account_data
            .get(Some(&body.room_id), sender_user, EventType::Tag)?
            .unwrap_or_else(|| TagEvent {
                content: TagEventContent {
                    tags: BTreeMap::new(),
                },
            })
            .content
            .tags,
    }
    .into())
}
