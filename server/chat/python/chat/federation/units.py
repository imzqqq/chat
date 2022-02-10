""" Defines the JSON structure of the protocol units used by the server to
server protocol.
"""

import logging
from typing import List, Optional

import attr

from chat.types import JsonDict

logger = logging.getLogger(__name__)


@attr.s(slots=True, frozen=True, auto_attribs=True)
class Edu:
    """An Edu represents a piece of data sent from one homeserver to another.

    In comparison to Pdus, Edus are not persisted for a long time on disk, are
    not meaningful beyond a given pair of homeservers, and don't have an
    internal ID or previous references graph.
    """

    edu_type: str
    content: dict
    origin: str
    destination: str

    def get_dict(self) -> JsonDict:
        return {
            "edu_type": self.edu_type,
            "content": self.content,
        }

    def get_internal_dict(self) -> JsonDict:
        return {
            "edu_type": self.edu_type,
            "content": self.content,
            "origin": self.origin,
            "destination": self.destination,
        }

    def get_context(self) -> str:
        return getattr(self, "content", {}).get("org.matrix.opentracing_context", "{}")

    def strip_context(self) -> None:
        getattr(self, "content", {})["org.matrix.opentracing_context"] = "{}"


def _none_to_list(edus: Optional[List[JsonDict]]) -> List[JsonDict]:
    if edus is None:
        return []
    return edus


@attr.s(slots=True, frozen=True, auto_attribs=True)
class Transaction:
    """A transaction is a list of Pdus and Edus to be sent to a remote home
    server with some extra metadata.

    Example transaction::

        {
            "origin": "foo",
            "prev_ids": ["abc", "def"],
            "pdus": [
                ...
            ],
        }

    """

    # Required keys.
    transaction_id: str
    origin: str
    destination: str
    origin_server_ts: int
    pdus: List[JsonDict] = attr.ib(factory=list, converter=_none_to_list)
    edus: List[JsonDict] = attr.ib(factory=list, converter=_none_to_list)

    def get_dict(self) -> JsonDict:
        """A JSON-ready dictionary of valid keys which aren't internal."""
        result = {
            "origin": self.origin,
            "origin_server_ts": self.origin_server_ts,
            "pdus": self.pdus,
        }
        if self.edus:
            result["edus"] = self.edus
        return result
