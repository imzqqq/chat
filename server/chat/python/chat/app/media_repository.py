import sys

from chat.app.generic_worker import start
from chat.util.logcontext import LoggingContext

if __name__ == "__main__":
    with LoggingContext("main"):
        start(sys.argv[1:])
