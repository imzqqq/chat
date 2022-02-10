"""
The storage layer is split up into multiple parts to allow Chat server to run
against different configurations of databases (e.g. single or multiple
databases). The `DatabasePool` class represents connections to a single physical
database. The `databases` are classes that talk directly to a `DatabasePool`
instance and have associated schemas, background updates, etc. On top of those
there are classes that provide high level interfaces that combine calls to
multiple `databases`.

There are also schemas that get applied to every database, regardless of the
data stores associated with them (e.g. the schema version tables), which are
stored in `chat.storage.schema`.
"""
from typing import TYPE_CHECKING

from chat.storage.databases import Databases
from chat.storage.databases.main import DataStore
from chat.storage.persist_events import EventsPersistenceStorage
from chat.storage.purge_events import PurgeEventsStorage
from chat.storage.state import StateGroupStorage

if TYPE_CHECKING:
    from chat.server import HomeServer


__all__ = ["Databases", "DataStore"]


class Storage:
    """The high level interfaces for talking to various storage layers."""

    def __init__(self, hs: "HomeServer", stores: Databases):
        # We include the main data store here mainly so that we don't have to
        # rewrite all the existing code to split it into high vs low level
        # interfaces.
        self.main = stores.main

        self.purge_events = PurgeEventsStorage(hs, stores)
        self.state = StateGroupStorage(hs, stores)

        self.persistence = None
        if stores.persist_events:
            self.persistence = EventsPersistenceStorage(hs, stores)
