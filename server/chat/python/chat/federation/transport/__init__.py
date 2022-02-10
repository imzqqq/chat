"""The transport layer is responsible for both sending transactions to remote
homeservers and receiving a variety of requests from other homeservers.

By default this is done over HTTPS (and all homeservers are required to
support HTTPS), however individual pairings of servers may decide to
communicate over a different (albeit still reliable) protocol.
"""
