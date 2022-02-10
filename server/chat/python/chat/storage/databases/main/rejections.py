import logging
from typing import Optional

from chat.storage._base import SQLBaseStore

logger = logging.getLogger(__name__)


class RejectionsStore(SQLBaseStore):
    async def get_rejection_reason(self, event_id: str) -> Optional[str]:
        return await self.db_pool.simple_select_one_onecol(
            table="rejections",
            retcol="reason",
            keyvalues={"event_id": event_id},
            allow_none=True,
            desc="get_rejection_reason",
        )
