import hashlib

import unpaddedbase64


def sha256_and_url_safe_base64(input_text: str) -> str:
    """SHA256 hash an input string, encode the digest as url-safe base64, and
    return

    Args:
        input_text: string to hash

    returns:
        A sha256 hashed and url-safe base64 encoded digest
    """
    digest = hashlib.sha256(input_text.encode()).digest()
    return unpaddedbase64.encode_base64(digest, urlsafe=True)
