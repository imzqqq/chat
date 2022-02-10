import logging
from typing import TYPE_CHECKING

from chat.events import EventBase
from chat.events.snapshot import EventContext
from chat.push.bulk_push_rule_evaluator import BulkPushRuleEvaluator
from chat.util.metrics import Measure

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class ActionGenerator:
    def __init__(self, hs: "HomeServer"):
        self.clock = hs.get_clock()
        self.bulk_evaluator = BulkPushRuleEvaluator(hs)
        # really we want to get all user ids and all profile tags too,
        # since we want the actions for each profile tag for every user and
        # also actions for a client with no profile tag for each user.
        # Currently the event stream doesn't support profile tags on an
        # event stream, so we just run the rules for a client with no profile
        # tag (ie. we just need all the users).

    async def handle_push_actions_for_event(
        self, event: EventBase, context: EventContext
    ) -> None:
        with Measure(self.clock, "action_for_event_by_user"):
            await self.bulk_evaluator.action_for_event_by_user(event, context)
