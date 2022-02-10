import logging
from typing import TYPE_CHECKING, Callable, Dict, Optional

from chat.push import Pusher, PusherConfig
from chat.push.emailpusher import EmailPusher
from chat.push.httppusher import HttpPusher
from chat.push.mailer import Mailer

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class PusherFactory:
    def __init__(self, hs: "HomeServer"):
        self.hs = hs
        self.config = hs.config

        self.pusher_types: Dict[str, Callable[[HomeServer, PusherConfig], Pusher]] = {
            "http": HttpPusher
        }

        logger.info("email enable notifs: %r", hs.config.email_enable_notifs)
        if hs.config.email_enable_notifs:
            self.mailers: Dict[str, Mailer] = {}

            self._notif_template_html = hs.config.email_notif_template_html
            self._notif_template_text = hs.config.email_notif_template_text

            self.pusher_types["email"] = self._create_email_pusher

            logger.info("defined email pusher type")

    def create_pusher(self, pusher_config: PusherConfig) -> Optional[Pusher]:
        kind = pusher_config.kind
        f = self.pusher_types.get(kind, None)
        if not f:
            return None
        logger.debug("creating %s pusher for %r", kind, pusher_config)
        return f(self.hs, pusher_config)

    def _create_email_pusher(
        self, _hs: "HomeServer", pusher_config: PusherConfig
    ) -> EmailPusher:
        app_name = self._app_name_from_pusherdict(pusher_config)
        mailer = self.mailers.get(app_name)
        if not mailer:
            mailer = Mailer(
                hs=self.hs,
                app_name=app_name,
                template_html=self._notif_template_html,
                template_text=self._notif_template_text,
            )
            self.mailers[app_name] = mailer
        return EmailPusher(self.hs, pusher_config, mailer)

    def _app_name_from_pusherdict(self, pusher_config: PusherConfig) -> str:
        data = pusher_config.data

        if isinstance(data, dict):
            brand = data.get("brand")
            if isinstance(brand, str):
                return brand

        return self.config.email_app_name
