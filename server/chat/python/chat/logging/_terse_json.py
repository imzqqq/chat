"""
Log formatters that output terse JSON.
"""
import json
import logging

_encoder = json.JSONEncoder(ensure_ascii=False, separators=(",", ":"))

# The properties of a standard LogRecord that should be ignored when generating
# JSON logs.
_IGNORED_LOG_RECORD_ATTRIBUTES = {
    "args",
    "asctime",
    "created",
    "exc_info",
    # exc_text isn't a public attribute, but is used to cache the result of formatException.
    "exc_text",
    "filename",
    "funcName",
    "levelname",
    "levelno",
    "lineno",
    "message",
    "module",
    "msecs",
    "msg",
    "name",
    "pathname",
    "process",
    "processName",
    "relativeCreated",
    "stack_info",
    "thread",
    "threadName",
}


class JsonFormatter(logging.Formatter):
    def format(self, record: logging.LogRecord) -> str:
        event = {
            "log": record.getMessage(),
            "namespace": record.name,
            "level": record.levelname,
        }

        return self._format(record, event)

    def _format(self, record: logging.LogRecord, event: dict) -> str:
        # Add attributes specified via the extra keyword to the logged event.
        for key, value in record.__dict__.items():
            if key not in _IGNORED_LOG_RECORD_ATTRIBUTES:
                event[key] = value

        return _encoder.encode(event)


class TerseJsonFormatter(JsonFormatter):
    def format(self, record: logging.LogRecord) -> str:
        event = {
            "log": record.getMessage(),
            "namespace": record.name,
            "level": record.levelname,
            "time": round(record.created, 2),
        }

        return self._format(record, event)
