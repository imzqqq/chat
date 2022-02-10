import logging
import traceback
from io import StringIO


class LogFormatter(logging.Formatter):
    """Log formatter which gives more detail for exceptions

    This is the same as the standard log formatter, except that when logging
    exceptions [typically via log.foo("msg", exc_info=1)], it prints the
    sequence that led up to the point at which the exception was caught.
    (Normally only stack frames between the point the exception was raised and
    where it was caught are logged).
    """

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

    def formatException(self, ei):
        sio = StringIO()
        (typ, val, tb) = ei

        # log the stack above the exception capture point if possible, but
        # check that we actually have an f_back attribute to work around
        # https://twistedmatrix.com/trac/ticket/9305

        if tb and hasattr(tb.tb_frame, "f_back"):
            sio.write("Capture point (most recent call last):\n")
            traceback.print_stack(tb.tb_frame.f_back, None, sio)

        traceback.print_exception(typ, val, tb, None, sio)
        s = sio.getvalue()
        sio.close()
        if s[-1:] == "\n":
            s = s[:-1]
        return s
