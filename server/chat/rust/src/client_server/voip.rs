use crate::ConduitResult;
use ruma::api::client::r0::voip::get_turn_server_info;
use std::time::Duration;

#[cfg(feature = "conduit_bin")]
use rocket::get;

/// # `GET /chat/client/r0/voip/turnServer`
///
/// TODO: Returns information about the recommended turn server.
#[cfg_attr(feature = "conduit_bin", get("/chat/client/r0/voip/turnServer"))]
#[tracing::instrument]
pub async fn turn_server_route() -> ConduitResult<get_turn_server_info::Response> {
    Ok(get_turn_server_info::Response {
        username: "".to_owned(),
        password: "".to_owned(),
        uris: Vec::new(),
        ttl: Duration::from_secs(60 * 60 * 24),
    }
    .into())
}
