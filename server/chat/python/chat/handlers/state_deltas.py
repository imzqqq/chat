import logging
from typing import TYPE_CHECKING, Optional

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class StateDeltasHandler:
    def __init__(self, hs: "HomeServer"):
        self.store = hs.get_datastore()

    async def _get_key_change(
        self,
        prev_event_id: Optional[str],
        event_id: Optional[str],
        key_name: str,
        public_value: str,
    ) -> Optional[bool]:
        """Given two events check if the `key_name` field in content changed
        from not matching `public_value` to doing so.

        For example, check if `history_visibility` (`key_name`) changed from
        `shared` to `world_readable` (`public_value`).

        Returns:
            None if the field in the events either both match `public_value`
            or if neither do, i.e. there has been no change.
            True if it didn't match `public_value` but now does
            False if it did match `public_value` but now doesn't
        """
        prev_event = None
        event = None
        if prev_event_id:
            prev_event = await self.store.get_event(prev_event_id, allow_none=True)

        if event_id:
            event = await self.store.get_event(event_id, allow_none=True)

        if not event and not prev_event:
            logger.debug("Neither event exists: %r %r", prev_event_id, event_id)
            return None

        prev_value = None
        value = None

        if prev_event:
            prev_value = prev_event.content.get(key_name)

        if event:
            value = event.content.get(key_name)

        logger.debug("prev_value: %r -> value: %r", prev_value, value)

        if value == public_value and prev_value != public_value:
            return True
        elif value != public_value and prev_value == public_value:
            return False
        else:
            return None
