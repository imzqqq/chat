import logging
import warnings
from io import StringIO
from unittest.mock import Mock

from pyperf import perf_counter

from twisted.internet.defer import Deferred
from twisted.internet.protocol import ServerFactory
from twisted.logger import LogBeginner, LogPublisher
from twisted.protocols.basic import LineOnlyReceiver

from chat.config.logger import _setup_stdlib_logging
from chat.logging import RemoteHandler
from chat.util import Clock


class LineCounter(LineOnlyReceiver):

    delimiter = b"\n"

    def __init__(self, *args, **kwargs):
        self.count = 0
        super().__init__(*args, **kwargs)

    def lineReceived(self, line):
        self.count += 1

        if self.count >= self.factory.wait_for and self.factory.on_done:
            on_done = self.factory.on_done
            self.factory.on_done = None
            on_done.callback(True)


async def main(reactor, loops):
    """
    Benchmark how long it takes to send `loops` messages.
    """
    servers = []

    def protocol():
        p = LineCounter()
        servers.append(p)
        return p

    logger_factory = ServerFactory.forProtocol(protocol)
    logger_factory.wait_for = loops
    logger_factory.on_done = Deferred()
    port = reactor.listenTCP(0, logger_factory, interface="127.0.0.1")

    # A fake homeserver config.
    class Config:
        server_name = "synmark-" + str(loops)
        no_redirect_stdio = True

    hs_config = Config()

    # To be able to sleep.
    clock = Clock(reactor)

    errors = StringIO()
    publisher = LogPublisher()
    mock_sys = Mock()
    beginner = LogBeginner(
        publisher, errors, mock_sys, warnings, initialBufferSize=loops
    )

    log_config = {
        "version": 1,
        "loggers": {"chat": {"level": "DEBUG", "handlers": ["tersejson"]}},
        "formatters": {"tersejson": {"class": "chat.logging.TerseJsonFormatter"}},
        "handlers": {
            "tersejson": {
                "class": "chat.logging.RemoteHandler",
                "host": "127.0.0.1",
                "port": port.getHost().port,
                "maximum_buffer": 100,
                "_reactor": reactor,
            }
        },
    }

    logger = logging.getLogger("chat.logging.test_terse_json")
    _setup_stdlib_logging(
        hs_config,
        log_config,
        logBeginner=beginner,
    )

    # Wait for it to connect...
    for handler in logging.getLogger("chat").handlers:
        if isinstance(handler, RemoteHandler):
            break
    else:
        raise RuntimeError("Improperly configured: no RemoteHandler found.")

    await handler._service.whenConnected()

    start = perf_counter()

    # Send a bunch of useful messages
    for i in range(0, loops):
        logger.info("test message %s", i)

        if len(handler._buffer) == handler.maximum_buffer:
            while len(handler._buffer) > handler.maximum_buffer / 2:
                await clock.sleep(0.01)

    await logger_factory.on_done

    end = perf_counter() - start

    handler.close()
    port.stopListening()

    return end
