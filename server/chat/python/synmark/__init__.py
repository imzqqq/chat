import sys

try:
    from twisted.internet.epollreactor import EPollReactor as Reactor
except ImportError:
    from twisted.internet.pollreactor import PollReactor as Reactor
from twisted.internet.main import installReactor


def make_reactor():
    """
    Instantiate and install a Twisted reactor suitable for testing (i.e. not the
    default global one).
    """
    reactor = Reactor()

    if "twisted.internet.reactor" in sys.modules:
        del sys.modules["twisted.internet.reactor"]
    installReactor(reactor)

    return reactor
