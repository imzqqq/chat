import logging

from typing_extensions import Literal


class MetadataFilter(logging.Filter):
    """Logging filter that adds constant values to each record.

    Args:
        metadata: Key-value pairs to add to each record.
    """

    def __init__(self, metadata: dict):
        self._metadata = metadata

    def filter(self, record: logging.LogRecord) -> Literal[True]:
        for key, value in self._metadata.items():
            setattr(record, key, value)
        return True
