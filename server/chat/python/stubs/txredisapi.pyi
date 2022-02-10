"""Contains *incomplete* type hints for txredisapi.
"""
from typing import Any, List, Optional, Type, Union

from twisted.internet import protocol

class RedisProtocol(protocol.Protocol):
    def publish(self, channel: str, message: bytes): ...
    async def ping(self) -> None: ...
    async def set(
        self,
        key: str,
        value: Any,
        expire: Optional[int] = None,
        pexpire: Optional[int] = None,
        only_if_not_exists: bool = False,
        only_if_exists: bool = False,
    ) -> None: ...
    async def get(self, key: str) -> Any: ...

class SubscriberProtocol(RedisProtocol):
    def __init__(self, *args, **kwargs): ...
    password: Optional[str]
    def subscribe(self, channels: Union[str, List[str]]): ...
    def connectionMade(self): ...
    def connectionLost(self, reason): ...

def lazyConnection(
    host: str = ...,
    port: int = ...,
    dbid: Optional[int] = ...,
    reconnect: bool = ...,
    charset: str = ...,
    password: Optional[str] = ...,
    connectTimeout: Optional[int] = ...,
    replyTimeout: Optional[int] = ...,
    convertNumbers: bool = ...,
) -> RedisProtocol: ...

class ConnectionHandler: ...

class RedisFactory(protocol.ReconnectingClientFactory):
    continueTrying: bool
    handler: RedisProtocol
    pool: List[RedisProtocol]
    replyTimeout: Optional[int]
    def __init__(
        self,
        uuid: str,
        dbid: Optional[int],
        poolsize: int,
        isLazy: bool = False,
        handler: Type = ConnectionHandler,
        charset: str = "utf-8",
        password: Optional[str] = None,
        replyTimeout: Optional[int] = None,
        convertNumbers: Optional[int] = True,
    ): ...
    def buildProtocol(self, addr) -> RedisProtocol: ...

class SubscriberFactory(RedisFactory):
    def __init__(self): ...
