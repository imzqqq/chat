from ._base import BaseDatabaseEngine, IncorrectDatabaseSetup
from .postgres import PostgresEngine
from .sqlite import Sqlite3Engine


def create_engine(database_config) -> BaseDatabaseEngine:
    name = database_config["name"]

    if name == "sqlite3":
        import sqlite3

        return Sqlite3Engine(sqlite3, database_config)

    if name == "psycopg2":
        # Note that psycopg2cffi-compat provides the psycopg2 module on pypy.
        import psycopg2  # type: ignore

        return PostgresEngine(psycopg2, database_config)

    raise RuntimeError("Unsupported database engine '%s'" % (name,))


__all__ = ["create_engine", "BaseDatabaseEngine", "IncorrectDatabaseSetup"]
