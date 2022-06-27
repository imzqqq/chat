import typing
from dataclasses import dataclass
from typing import Union
from urllib.parse import urlparse

from sqlalchemy.orm import Session
from sqlalchemy.orm import joinedload

from app import activitypub as ap
from app import media

if typing.TYPE_CHECKING:
    from app.models import Actor as ActorModel


def _handle(raw_actor: ap.RawObject) -> str:
    ap_id = ap.get_id(raw_actor["id"])
    domain = urlparse(ap_id)
    if not domain.hostname:
        raise ValueError(f"Invalid actor ID {ap_id}")

    return f'@{raw_actor["preferredUsername"]}@{domain.hostname}'  # type: ignore


class Actor:
    @property
    def ap_actor(self) -> ap.RawObject:
        raise NotImplementedError()

    @property
    def ap_id(self) -> str:
        return ap.get_id(self.ap_actor["id"])

    @property
    def name(self) -> str | None:
        return self.ap_actor.get("name")

    @property
    def summary(self) -> str | None:
        return self.ap_actor.get("summary")

    @property
    def url(self) -> str | None:
        return self.ap_actor.get("url") or self.ap_actor["id"]

    @property
    def preferred_username(self) -> str:
        return self.ap_actor["preferredUsername"]

    @property
    def display_name(self) -> str:
        return self.name or self.preferred_username

    @property
    def handle(self) -> str:
        return _handle(self.ap_actor)

    @property
    def ap_type(self) -> str:
        raise NotImplementedError()

    @property
    def inbox_url(self) -> str:
        return self.ap_actor["inbox"]

    @property
    def shared_inbox_url(self) -> str | None:
        return self.ap_actor.get("endpoints", {}).get("sharedInbox")

    @property
    def icon_url(self) -> str | None:
        return self.ap_actor.get("icon", {}).get("url")

    @property
    def icon_media_type(self) -> str | None:
        return self.ap_actor.get("icon", {}).get("mediaType")

    @property
    def public_key_as_pem(self) -> str:
        return self.ap_actor["publicKey"]["publicKeyPem"]

    @property
    def public_key_id(self) -> str:
        return self.ap_actor["publicKey"]["id"]

    @property
    def proxied_icon_url(self) -> str:
        if self.icon_url:
            return media.proxied_media_url(self.icon_url)
        else:
            return "/static/nopic.png"

    @property
    def resized_icon_url(self) -> str:
        if self.icon_url:
            return media.resized_media_url(self.icon_url, 50)
        else:
            return "/static/nopic.png"

    @property
    def tags(self) -> list[ap.RawObject]:
        return self.ap_actor.get("tag", [])

    @property
    def followers_collection_id(self) -> str:
        return self.ap_actor["followers"]


class RemoteActor(Actor):
    def __init__(self, ap_actor: ap.RawObject) -> None:
        if (ap_type := ap_actor.get("type")) not in ap.ACTOR_TYPES:
            raise ValueError(f"Unexpected actor type: {ap_type}")

        self._ap_actor = ap_actor
        self._ap_type = ap_type

    @property
    def ap_actor(self) -> ap.RawObject:
        return self._ap_actor

    @property
    def ap_type(self) -> str:
        return self._ap_type

    @property
    def is_from_db(self) -> bool:
        return False


LOCAL_ACTOR = RemoteActor(ap_actor=ap.ME)


def save_actor(db: Session, ap_actor: ap.RawObject) -> "ActorModel":
    from app import models

    if ap_type := ap_actor.get("type") not in ap.ACTOR_TYPES:
        raise ValueError(f"Invalid type {ap_type} for actor {ap_actor}")

    actor = models.Actor(
        ap_id=ap_actor["id"],
        ap_actor=ap_actor,
        ap_type=ap_actor["type"],
        handle=_handle(ap_actor),
    )
    db.add(actor)
    db.commit()
    db.refresh(actor)
    return actor


def fetch_actor(db: Session, actor_id: str) -> "ActorModel":
    from app import models

    existing_actor = (
        db.query(models.Actor).filter(models.Actor.ap_id == actor_id).one_or_none()
    )
    if existing_actor:
        return existing_actor

    ap_actor = ap.get(actor_id)
    return save_actor(db, ap_actor)


@dataclass
class ActorMetadata:
    ap_actor_id: str
    is_following: bool
    is_follower: bool
    is_follow_request_sent: bool
    outbox_follow_ap_id: str | None
    inbox_follow_ap_id: str | None


ActorsMetadata = dict[str, ActorMetadata]


def get_actors_metadata(
    db: Session,
    actors: list[Union["ActorModel", "RemoteActor"]],
) -> ActorsMetadata:
    from app import models

    ap_actor_ids = [actor.ap_id for actor in actors]
    followers = {
        follower.ap_actor_id: follower.inbox_object.ap_id
        for follower in db.query(models.Follower)
        .filter(models.Follower.ap_actor_id.in_(ap_actor_ids))
        .options(joinedload(models.Follower.inbox_object))
        .all()
    }
    following = {
        following.ap_actor_id
        for following in db.query(models.Following.ap_actor_id)
        .filter(models.Following.ap_actor_id.in_(ap_actor_ids))
        .all()
    }
    sent_follow_requests = {
        follow_req.ap_object["object"]: follow_req.ap_id
        for follow_req in db.query(
            models.OutboxObject.ap_object, models.OutboxObject.ap_id
        )
        .filter(
            models.OutboxObject.ap_type == "Follow",
            models.OutboxObject.undone_by_outbox_object_id.is_(None),
        )
        .all()
    }
    idx: ActorsMetadata = {}
    for actor in actors:
        if not actor.ap_id:
            raise ValueError("Should never happen")
        idx[actor.ap_id] = ActorMetadata(
            ap_actor_id=actor.ap_id,
            is_following=actor.ap_id in following,
            is_follower=actor.ap_id in followers,
            is_follow_request_sent=actor.ap_id in sent_follow_requests,
            outbox_follow_ap_id=sent_follow_requests.get(actor.ap_id),
            inbox_follow_ap_id=followers.get(actor.ap_id),
        )
    return idx
