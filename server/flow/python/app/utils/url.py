import functools
import ipaddress
import socket
from urllib.parse import urlparse

from loguru import logger

from app.config import DEBUG


class InvalidURLError(Exception):
    pass


@functools.lru_cache
def _getaddrinfo(hostname: str, port: int) -> str:
    try:
        ip_address = str(ipaddress.ip_address(hostname))
    except ValueError:
        try:
            ip_address = socket.getaddrinfo(hostname, port)[0][4][0]
            logger.debug(f"DNS lookup: {hostname} -> {ip_address}")
        except socket.gaierror:
            logger.exception(f"failed to lookup addr info for {hostname}")
            raise

    return ip_address


def is_url_valid(url: str) -> bool:
    """Implements basic SSRF protection."""
    parsed = urlparse(url)
    if parsed.scheme not in ["http", "https"]:
        return False

    # XXX in debug mode, we want to allow requests to localhost to test the
    # federation with local instances
    if DEBUG:  # pragma: no cover
        return True

    if not parsed.hostname or parsed.hostname.lower() in ["localhost"]:
        return False

    ip_address = _getaddrinfo(
        parsed.hostname, parsed.port or (80 if parsed.scheme == "http" else 443)
    )
    logger.debug(f"{ip_address=}")

    if ipaddress.ip_address(ip_address).is_private:
        logger.info(f"rejecting private URL {url} -> {ip_address}")
        return False

    return True


def check_url(url: str, debug: bool = False) -> None:
    logger.debug(f"check_url {url=}")
    if not is_url_valid(url):
        raise InvalidURLError(f'"{url}" is invalid')

    return None
