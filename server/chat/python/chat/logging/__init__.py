import logging

from chat.logging._remote import RemoteHandler
from chat.logging._terse_json import JsonFormatter, TerseJsonFormatter

# These are imported to allow for nicer logging configuration files.
__all__ = ["RemoteHandler", "JsonFormatter", "TerseJsonFormatter"]

# Debug logger for https://github.com/matrix-org/chat/issues/9533 etc
issue9533_logger = logging.getLogger("chat.9533_debug")
