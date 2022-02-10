from typing import Any, Iterator, List, Mapping, Optional, Sequence, Tuple, Union

from typing_extensions import Protocol

"""
Some very basic protocol definitions for the DB-API2 classes specified in PEP-249
"""

_Parameters = Union[Sequence[Any], Mapping[str, Any]]


class Cursor(Protocol):
    def execute(self, sql: str, parameters: _Parameters = ...) -> Any:
        ...

    def executemany(self, sql: str, parameters: Sequence[_Parameters]) -> Any:
        ...

    def fetchone(self) -> Optional[Tuple]:
        ...

    def fetchmany(self, size: Optional[int] = ...) -> List[Tuple]:
        ...

    def fetchall(self) -> List[Tuple]:
        ...

    @property
    def description(
        self,
    ) -> Optional[
        Sequence[
            # Note that this is an approximate typing based on sqlite3 and other
            # drivers, and may not be entirely accurate.
            Tuple[
                str,
                Optional[Any],
                Optional[int],
                Optional[int],
                Optional[int],
                Optional[int],
                Optional[int],
            ]
        ]
    ]:
        ...

    @property
    def rowcount(self) -> int:
        return 0

    def __iter__(self) -> Iterator[Tuple]:
        ...

    def close(self) -> None:
        ...


class Connection(Protocol):
    def cursor(self) -> Cursor:
        ...

    def close(self) -> None:
        ...

    def commit(self) -> None:
        ...

    def rollback(self) -> None:
        ...

    def __enter__(self) -> "Connection":
        ...

    def __exit__(self, exc_type, exc_value, traceback) -> Optional[bool]:
        ...
