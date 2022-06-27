from typing import Any
from urllib.parse import urlparse

import httpx
from loguru import logger

from app import config


def webfinger(
    resource: str,
) -> dict[str, Any] | None:  # noqa: C901
    """Mastodon-like WebFinger resolution to retrieve the activity stream Actor URL."""
    logger.info(f"performing webfinger resolution for {resource}")
    protos = ["https", "http"]
    if resource.startswith("http://"):
        protos.reverse()
        host = urlparse(resource).netloc
    elif resource.startswith("https://"):
        host = urlparse(resource).netloc
    else:
        if resource.startswith("acct:"):
            resource = resource[5:]
        if resource.startswith("@"):
            resource = resource[1:]
        _, host = resource.split("@", 1)
        resource = "acct:" + resource

    is_404 = False

    for i, proto in enumerate(protos):
        try:
            url = f"{proto}://{host}/.well-known/webfinger"
            resp = httpx.get(
                url,
                params={"resource": resource},
                headers={
                    "User-Agent": config.USER_AGENT,
                },
            )
            break
        except httpx.HTTPStatusError as http_error:
            logger.exception("HTTP error")
            if http_error.response.status_code in [403, 404, 410]:
                is_404 = True
                continue
            raise
        except httpx.HTTPError:
            logger.exception("req failed")
            # If we tried https first and the domain is "http only"
            if i == 0:
                continue
            break
    if is_404:
        return None

    return resp.json()


def get_remote_follow_template(resource: str) -> str | None:
    data = webfinger(resource)
    if data is None:
        return None
    for link in data["links"]:
        if link.get("rel") == "http://ostatus.org/schema/1.0/subscribe":
            return link.get("template")
    return None


def get_actor_url(resource: str) -> str | None:
    """Mastodon-like WebFinger resolution to retrieve the activity stream Actor URL.

    Returns:
        the Actor URL or None if the resolution failed.
    """
    data = webfinger(resource)
    if data is None:
        return None
    for link in data["links"]:
        if (
            link.get("rel") == "self"
            and link.get("type") == "application/activity+json"
        ):
            return link.get("href")
    return None
