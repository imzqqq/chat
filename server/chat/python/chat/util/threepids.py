import logging
import re

logger = logging.getLogger(__name__)


# it's unclear what the maximum length of an email address is. RFC3696 (as corrected
# by errata) says:
#    the upper limit on address lengths should normally be considered to be 254.
#
# In practice, mail servers appear to be more tolerant and allow 400 characters
# or so. Let's allow 500, which should be plenty for everyone.
#
MAX_EMAIL_ADDRESS_LENGTH = 500


def check_3pid_allowed(hs, medium, address):
    """Checks whether a given format of 3PID is allowed to be used on this HS

    Args:
        hs (chat.server.HomeServer): server
        medium (str): 3pid medium - e.g. email, msisdn
        address (str): address within that medium (e.g. "wotan@matrix.org")
            msisdns need to first have been canonicalised
    Returns:
        bool: whether the 3PID medium/address is allowed to be added to this HS
    """

    if hs.config.allowed_local_3pids:
        for constraint in hs.config.allowed_local_3pids:
            logger.debug(
                "Checking 3PID %s (%s) against %s (%s)",
                address,
                medium,
                constraint["pattern"],
                constraint["medium"],
            )
            if medium == constraint["medium"] and re.match(
                constraint["pattern"], address
            ):
                return True
    else:
        return True

    return False


def canonicalise_email(address: str) -> str:
    """'Canonicalise' email address
    Case folding of local part of email address and lowercase domain part
    See MSC2265, https://github.com/matrix-org/matrix-doc/pull/2265

    Args:
        address: email address to be canonicalised
    Returns:
        The canonical form of the email address
    Raises:
        ValueError if the address could not be parsed.
    """

    address = address.strip()

    parts = address.split("@")
    if len(parts) != 2:
        logger.debug("Couldn't parse email address %s", address)
        raise ValueError("Unable to parse email address")

    return parts[0].casefold() + "@" + parts[1].lower()


def validate_email(address: str) -> str:
    """Does some basic validation on an email address.

    Returns the canonicalised email, as returned by `canonicalise_email`.

    Raises a ValueError if the email is invalid.
    """
    # First we try canonicalising in case that fails
    address = canonicalise_email(address)

    # Email addresses have to be at least 3 characters.
    if len(address) < 3:
        raise ValueError("Unable to parse email address")

    if len(address) > MAX_EMAIL_ADDRESS_LENGTH:
        raise ValueError("Unable to parse email address")

    return address
