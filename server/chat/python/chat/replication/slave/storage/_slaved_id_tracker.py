from typing import List, Optional, Tuple

from chat.storage.types import Connection
from chat.storage.util.id_generators import _load_current_id


class SlavedIdTracker:
    def __init__(
        self,
        db_conn: Connection,
        table: str,
        column: str,
        extra_tables: Optional[List[Tuple[str, str]]] = None,
        step: int = 1,
    ):
        self.step = step
        self._current = _load_current_id(db_conn, table, column, step)
        if extra_tables:
            for table, column in extra_tables:
                self.advance(None, _load_current_id(db_conn, table, column))

    def advance(self, instance_name: Optional[str], new_id: int):
        self._current = (max if self.step > 0 else min)(self._current, new_id)

    def get_current_token(self) -> int:
        """

        Returns:
            int
        """
        return self._current

    def get_current_token_for_writer(self, instance_name: str) -> int:
        """Returns the position of the given writer.

        For streams with single writers this is equivalent to
        `get_current_token`.
        """
        return self.get_current_token()
