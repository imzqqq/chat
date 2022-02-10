"""Defines all the valid streams that clients can subscribe to, and the format
of the rows returned by each stream.

Each stream is defined by the following information:

    stream name:        The name of the stream
    row type:           The type that is used to serialise/deserialse the row
    current_token:      The function that returns the current token for the stream
    update_function:    The function that returns a list of updates between two tokens
"""

from chat.replication.tcp.streams._base import (
    AccountDataStream,
    BackfillStream,
    CachesStream,
    DeviceListsStream,
    GroupServerStream,
    PresenceFederationStream,
    PresenceStream,
    PushersStream,
    PushRulesStream,
    ReceiptsStream,
    Stream,
    TagAccountDataStream,
    ToDeviceStream,
    TypingStream,
    UserSignatureStream,
)
from chat.replication.tcp.streams.events import EventsStream
from chat.replication.tcp.streams.federation import FederationStream

STREAMS_MAP = {
    stream.NAME: stream
    for stream in (
        EventsStream,
        BackfillStream,
        PresenceStream,
        PresenceFederationStream,
        TypingStream,
        ReceiptsStream,
        PushRulesStream,
        PushersStream,
        CachesStream,
        DeviceListsStream,
        ToDeviceStream,
        FederationStream,
        TagAccountDataStream,
        AccountDataStream,
        GroupServerStream,
        UserSignatureStream,
    )
}

__all__ = [
    "STREAMS_MAP",
    "Stream",
    "BackfillStream",
    "PresenceStream",
    "PresenceFederationStream",
    "TypingStream",
    "ReceiptsStream",
    "PushRulesStream",
    "PushersStream",
    "CachesStream",
    "DeviceListsStream",
    "ToDeviceStream",
    "TagAccountDataStream",
    "AccountDataStream",
    "GroupServerStream",
    "UserSignatureStream",
]
