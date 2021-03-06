import logging
import sys

from chat import python_dependencies  # noqa: E402

logger = logging.getLogger(__name__)

try:
    python_dependencies.check_requirements()
except python_dependencies.DependencyException as e:
    sys.stderr.writelines(
        e.message  # noqa: B306, DependencyException.message is a property
    )
    sys.exit(1)


def check_bind_error(e, address, bind_addresses):
    """
    This method checks an exception occurred while binding on 0.0.0.0.
    If :: is specified in the bind addresses a warning is shown.
    The exception is still raised otherwise.

    Binding on both 0.0.0.0 and :: causes an exception on Linux and macOS
    because :: binds on both IPv4 and IPv6 (as per RFC 3493).
    When binding on 0.0.0.0 after :: this can safely be ignored.

    Args:
        e (Exception): Exception that was caught.
        address (str): Address on which binding was attempted.
        bind_addresses (list): Addresses on which the service listens.
    """
    if address == "0.0.0.0" and "::" in bind_addresses:
        logger.warning(
            "Failed to listen on 0.0.0.0, continuing because listening on [::]"
        )
    else:
        raise e
