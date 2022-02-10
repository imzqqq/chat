"""This module implements the TCP replication protocol used by chat to
communicate between the master process and its workers (when they're enabled).

Further details can be found in docs/tcp_replication.rst


Structure of the module:
 * handler.py  - the classes used to handle sending/receiving commands to
                 replication
 * command.py  - the definitions of all the valid commands
 * protocol.py - the TCP protocol classes
 * resource.py - handles streaming stream updates to replications
 * streams/    - the definitions of all the valid streams


The general interaction of the classes are:

        +---------------------+
        | ReplicationStreamer |
        +---------------------+
                    |
                    v
        +---------------------------+     +----------------------+
        | ReplicationCommandHandler |---->|ReplicationDataHandler|
        +---------------------------+     +----------------------+
                    | ^
                    v |
            +-------------+
            | Protocols   |
            | (TCP/redis) |
            +-------------+

Where the ReplicationDataHandler (or subclasses) handles incoming stream
updates.
"""
