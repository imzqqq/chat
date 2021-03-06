import abc
from typing import Generic, TypeVar

from chat.storage.types import Connection


class IncorrectDatabaseSetup(RuntimeError):
    pass


ConnectionType = TypeVar("ConnectionType", bound=Connection)


class BaseDatabaseEngine(Generic[ConnectionType], metaclass=abc.ABCMeta):
    def __init__(self, module, database_config: dict):
        self.module = module

    @property
    @abc.abstractmethod
    def single_threaded(self) -> bool:
        ...

    @property
    @abc.abstractmethod
    def can_native_upsert(self) -> bool:
        """
        Do we support native UPSERTs?
        """
        ...

    @property
    @abc.abstractmethod
    def supports_using_any_list(self) -> bool:
        """
        Do we support using `a = ANY(?)` and passing a list
        """
        ...

    @property
    @abc.abstractmethod
    def supports_returning(self) -> bool:
        """Do we support the `RETURNING` clause in insert/update/delete?"""
        ...

    @abc.abstractmethod
    def check_database(
        self, db_conn: ConnectionType, allow_outdated_version: bool = False
    ) -> None:
        ...

    @abc.abstractmethod
    def check_new_database(self, txn) -> None:
        """Gets called when setting up a brand new database. This allows us to
        apply stricter checks on new databases versus existing database.
        """
        ...

    @abc.abstractmethod
    def convert_param_style(self, sql: str) -> str:
        ...

    @abc.abstractmethod
    def on_new_connection(self, db_conn: ConnectionType) -> None:
        ...

    @abc.abstractmethod
    def is_deadlock(self, error: Exception) -> bool:
        ...

    @abc.abstractmethod
    def is_connection_closed(self, conn: ConnectionType) -> bool:
        ...

    @abc.abstractmethod
    def lock_table(self, txn, table: str) -> None:
        ...

    @property
    @abc.abstractmethod
    def server_version(self) -> str:
        """Gets a string giving the server version. For example: '3.22.0'"""
        ...

    @abc.abstractmethod
    def in_transaction(self, conn: Connection) -> bool:
        """Whether the connection is currently in a transaction."""
        ...

    @abc.abstractmethod
    def attempt_to_set_autocommit(self, conn: Connection, autocommit: bool):
        """Attempt to set the connections autocommit mode.

        When True queries are run outside of transactions.

        Note: This has no effect on SQLite3, so callers still need to
        commit/rollback the connections.
        """
        ...
