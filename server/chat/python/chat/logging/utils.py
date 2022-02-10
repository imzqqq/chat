import logging
from functools import wraps
from inspect import getcallargs

_TIME_FUNC_ID = 0


def _log_debug_as_f(f, msg, msg_args):
    name = f.__module__
    logger = logging.getLogger(name)

    if logger.isEnabledFor(logging.DEBUG):
        lineno = f.__code__.co_firstlineno
        pathname = f.__code__.co_filename

        record = logger.makeRecord(
            name=name,
            level=logging.DEBUG,
            fn=pathname,
            lno=lineno,
            msg=msg,
            args=msg_args,
            exc_info=None,
        )

        logger.handle(record)


def log_function(f):
    """Function decorator that logs every call to that function."""
    func_name = f.__name__

    @wraps(f)
    def wrapped(*args, **kwargs):
        name = f.__module__
        logger = logging.getLogger(name)
        level = logging.DEBUG

        if logger.isEnabledFor(level):
            bound_args = getcallargs(f, *args, **kwargs)

            def format(value):
                r = str(value)
                if len(r) > 50:
                    r = r[:50] + "..."
                return r

            func_args = ["%s=%s" % (k, format(v)) for k, v in bound_args.items()]

            msg_args = {"func_name": func_name, "args": ", ".join(func_args)}

            _log_debug_as_f(f, "Invoked '%(func_name)s' with args: %(args)s", msg_args)

        return f(*args, **kwargs)

    wrapped.__name__ = func_name
    return wrapped
