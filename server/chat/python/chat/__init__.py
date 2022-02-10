""" This is a reference implementation of a homeserver.
"""

import json
import os
import sys

# Check that we're not running on an unsupported Python version.
if sys.version_info < (3, 6):
    print("Chat server requires Python 3.6 or above.")
    sys.exit(1)

# Twisted and canonicaljson will fail to import when this file is executed to
# get the __version__ during a fresh install. That's OK and subsequent calls to
# actually start chat server will import these libraries fine.
try:
    from twisted.internet import protocol
    from twisted.internet.protocol import Factory
    from twisted.names.dns import DNSDatagramProtocol

    protocol.Factory.noisy = False
    Factory.noisy = False
    DNSDatagramProtocol.noisy = False
except ImportError:
    pass

# Use the standard library json implementation instead of simplejson.
try:
    from canonicaljson import set_json_library

    set_json_library(json)
except ImportError:
    pass

__version__ = "1.41.1"

if bool(os.environ.get("SYNAPSE_TEST_PATCH_LOG_CONTEXTS", False)):
    # We import here so that we don't have to install a bunch of deps when
    # running the packaging tox test.
    from chat.util.patch_inline_callbacks import do_patch

    do_patch()
