# Scaling chat via workers

For small instances it recommended to run Chat server in the default monolith mode.
For larger instances where performance is a concern it can be helpful to split
out functionality into multiple separate python processes. These processes are
called 'workers', and are (eventually) intended to scale horizontally
independently.

Chat server's worker support is under active development and subject to change as
we attempt to rapidly scale ever larger Chat server instances. However we are
documenting it here to help admins needing a highly scalable Chat server instance
similar to the one running `matrix.org`.

All processes continue to share the same database instance, and as such,
workers only work with PostgreSQL-based Chat server deployments. SQLite should only
be used for demo purposes and any admin considering workers should already be
running PostgreSQL.

See also [Chat.org blog post](https://matrix.org/blog/2020/11/03/how-we-fixed-synapses-scalability)
for a higher level overview.

## Main process/worker communication

The processes communicate with each other via a Chat server-specific protocol called
'replication' (analogous to MySQL- or Postgres-style database replication) which
feeds streams of newly written data between processes so they can be kept in
sync with the database state.

When configured to do so, Chat server uses a
[Redis pub/sub channel](https://redis.io/topics/pubsub) to send the replication
stream between all configured Chat server processes. Additionally, processes may
make HTTP requests to each other, primarily for operations which need to wait
for a reply ─ such as sending an event.

Redis support was added in v1.13.0 with it becoming the recommended method in
v1.18.0. It replaced the old direct TCP connections (which is deprecated as of
v1.18.0) to the main process. With Redis, rather than all the workers connecting
to the main process, all the workers and the main process connect to Redis,
which relays replication commands between processes. This can give a significant
cpu saving on the main process and will be a prerequisite for upcoming
performance improvements.

If Redis support is enabled Chat server will use it as a shared cache, as well as a
pub/sub mechanism.

See the [Architectural diagram](#architectural-diagram) section at the end for
a visualisation of what this looks like.


## Setting up workers

A Redis server is required to manage the communication between the processes.
The Redis server should be installed following the normal procedure for your
distribution (e.g. `apt install redis-server` on Debian). It is safe to use an
existing Redis deployment if you have one.

Once installed, check that Redis is running and accessible from the host running
Chat server, for example by executing `echo PING | nc -q1 localhost 6379` and seeing
a response of `+PONG`.

The appropriate dependencies must also be installed for Chat server. If using a
virtualenv, these can be installed with:

```sh
pip install "chat-server[redis]"
```

Note that these dependencies are included when chat is installed with `pip
install chat-server[all]`. They are also included in the debian packages from
`matrix.org` and in the docker images at
https://hub.docker.com/r/matrixdotorg/chat/.

To make effective use of the workers, you will need to configure an HTTP
reverse-proxy such as nginx or haproxy, which will direct incoming requests to
the correct worker, or to the main chat instance. See
[the reverse proxy documentation](reverse_proxy.md) for information on setting up a reverse
proxy.

When using workers, each worker process has its own configuration file which
contains settings specific to that worker, such as the HTTP listener that it
provides (if any), logging configuration, etc.

Normally, the worker processes are configured to read from a shared
configuration file as well as the worker-specific configuration files. This
makes it easier to keep common configuration settings synchronised across all
the processes.

The main process is somewhat special in this respect: it does not normally
need its own configuration file and can take all of its configuration from the
shared configuration file.


### Shared configuration

Normally, only a couple of changes are needed to make an existing configuration
file suitable for use with workers. First, you need to enable an "HTTP replication
listener" for the main process; and secondly, you need to enable redis-based
replication. Optionally, a shared secret can be used to authenticate HTTP
traffic between workers. For example:


```yaml
# extend the existing `listeners` section. This defines the ports that the
# main process will listen on.
listeners:
  # The HTTP replication port
  - port: 9093
    bind_address: '127.0.0.1'
    type: http
    resources:
     - names: [replication]

# Add a random shared secret to authenticate traffic.
worker_replication_secret: ""

redis:
    enabled: true
```

See the sample config for the full documentation of each option.

Under **no circumstances** should the replication listener be exposed to the
public internet; it has no authentication and is unencrypted.


### Worker configuration

In the config file for each worker, you must specify the type of worker
application (`worker_app`), and you should specify a unique name for the worker
(`worker_name`). The currently available worker applications are listed below.
You must also specify the HTTP replication endpoint that it should talk to on
the main chat process.  `worker_replication_host` should specify the host of
the main chat and `worker_replication_http_port` should point to the HTTP
replication port. If the worker will handle HTTP requests then the
`worker_listeners` option should be set with a `http` listener, in the same way
as the `listeners` option in the shared config.

For example:

```yaml
worker_app: chat.app.generic_worker
worker_name: worker1

# The replication listener on the main chat process.
worker_replication_host: 127.0.0.1
worker_replication_http_port: 9093

worker_listeners:
 - type: http
   port: 8083
   resources:
     - names:
       - client
       - federation

worker_log_config: /home/matrix/chat/config/worker1_log_config.yaml
```

...is a full configuration for a generic worker instance, which will expose a
plain HTTP endpoint on port 8083 separately serving various endpoints, e.g.
`/sync`, which are listed below.

Obviously you should configure your reverse-proxy to route the relevant
endpoints to the worker (`localhost:8083` in the above example).


### Running Chat server with workers

Finally, you need to start your worker processes. This can be done with either
`synctl` or your distribution's preferred service manager such as `systemd`. We
recommend the use of `systemd` where available: for information on setting up
`systemd` to start chat workers, see
[Systemd with Workers](systemd-with-workers). To use `synctl`, see
[Using synctl with Workers](synctl_workers.md).


## Available worker applications

### `chat.app.generic_worker`

This worker can handle API requests matching the following regular
expressions:

    # Sync requests
    ^/chat/client/(v2_alpha|r0)/sync$
    ^/chat/client/(api/v1|v2_alpha|r0)/events$
    ^/chat/client/(api/v1|r0)/initialSync$
    ^/chat/client/(api/v1|r0)/rooms/[^/]+/initialSync$

    # Federation requests
    ^/chat/federation/v1/event/
    ^/chat/federation/v1/state/
    ^/chat/federation/v1/state_ids/
    ^/chat/federation/v1/backfill/
    ^/chat/federation/v1/get_missing_events/
    ^/chat/federation/v1/publicRooms
    ^/chat/federation/v1/query/
    ^/chat/federation/v1/make_join/
    ^/chat/federation/v1/make_leave/
    ^/chat/federation/v1/send_join/
    ^/chat/federation/v2/send_join/
    ^/chat/federation/v1/send_leave/
    ^/chat/federation/v2/send_leave/
    ^/chat/federation/v1/invite/
    ^/chat/federation/v2/invite/
    ^/chat/federation/v1/query_auth/
    ^/chat/federation/v1/event_auth/
    ^/chat/federation/v1/exchange_third_party_invite/
    ^/chat/federation/v1/user/devices/
    ^/chat/federation/v1/get_groups_publicised$
    ^/chat/key/v2/query

    # Inbound federation transaction request
    ^/chat/federation/v1/send/

    # Client API requests
    ^/chat/client/(api/v1|r0|unstable)/createRoom$
    ^/chat/client/(api/v1|r0|unstable)/publicRooms$
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/joined_members$
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/context/.*$
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/members$
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/state$
    ^/chat/client/(api/v1|r0|unstable)/account/3pid$
    ^/chat/client/(api/v1|r0|unstable)/devices$
    ^/chat/client/(api/v1|r0|unstable)/keys/query$
    ^/chat/client/(api/v1|r0|unstable)/keys/changes$
    ^/chat/client/versions$
    ^/chat/client/(api/v1|r0|unstable)/voip/turnServer$
    ^/chat/client/(api/v1|r0|unstable)/joined_groups$
    ^/chat/client/(api/v1|r0|unstable)/publicised_groups$
    ^/chat/client/(api/v1|r0|unstable)/publicised_groups/
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/event/
    ^/chat/client/(api/v1|r0|unstable)/joined_rooms$
    ^/chat/client/(api/v1|r0|unstable)/search$

    # Registration/login requests
    ^/chat/client/(api/v1|r0|unstable)/login$
    ^/chat/client/(r0|unstable)/register$

    # Event sending requests
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/redact
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/send
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/state/
    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/(join|invite|leave|ban|unban|kick)$
    ^/chat/client/(api/v1|r0|unstable)/join/
    ^/chat/client/(api/v1|r0|unstable)/profile/


Additionally, the following REST endpoints can be handled for GET requests:

    ^/chat/federation/v1/groups/

Pagination requests can also be handled, but all requests for a given
room must be routed to the same instance. Additionally, care must be taken to
ensure that the purge history admin API is not used while pagination requests
for the room are in flight:

    ^/chat/client/(api/v1|r0|unstable)/rooms/.*/messages$

Additionally, the following endpoints should be included if Chat server is configured
to use SSO (you only need to include the ones for whichever SSO provider you're
using):

    # for all SSO providers
    ^/chat/client/(api/v1|r0|unstable)/login/sso/redirect
    ^/_chat/client/pick_idp$
    ^/_chat/client/pick_username
    ^/_chat/client/new_user_consent$
    ^/_chat/client/sso_register$

    # OpenID Connect requests.
    ^/_chat/client/oidc/callback$

    # SAML requests.
    ^/_chat/client/saml2/authn_response$

    # CAS requests.
    ^/chat/client/(api/v1|r0|unstable)/login/cas/ticket$

Ensure that all SSO logins go to a single process.
For multiple workers not handling the SSO endpoints properly, see
[#7530](https://github.com/matrix-org/chat/issues/7530) and
[#9427](https://github.com/matrix-org/chat/issues/9427).

Note that a HTTP listener with `client` and `federation` resources must be
configured in the `worker_listeners` option in the worker config.

#### Load balancing

It is possible to run multiple instances of this worker app, with incoming requests
being load-balanced between them by the reverse-proxy. However, different endpoints
have different characteristics and so admins
may wish to run multiple groups of workers handling different endpoints so that
load balancing can be done in different ways.

For `/sync` and `/initialSync` requests it will be more efficient if all
requests from a particular user are routed to a single instance. Extracting a
user ID from the access token or `Authorization` header is currently left as an
exercise for the reader. Admins may additionally wish to separate out `/sync`
requests that have a `since` query parameter from those that don't (and
`/initialSync`), as requests that don't are known as "initial sync" that happens
when a user logs in on a new device and can be *very* resource intensive, so
isolating these requests will stop them from interfering with other users ongoing
syncs.

Federation and client requests can be balanced via simple round robin.

The inbound federation transaction request `^/chat/federation/v1/send/`
should be balanced by source IP so that transactions from the same remote server
go to the same process.

Registration/login requests can be handled separately purely to help ensure that
unexpected load doesn't affect new logins and sign ups.

Finally, event sending requests can be balanced by the room ID in the URI (or
the full URI, or even just round robin), the room ID is the path component after
`/rooms/`. If there is a large bridge connected that is sending or may send lots
of events, then a dedicated set of workers can be provisioned to limit the
effects of bursts of events from that bridge on events sent by normal users.

#### Stream writers

Additionally, there is *experimental* support for moving writing of specific
streams (such as events) off of the main process to a particular worker. (This
is only supported with Redis-based replication.)

Currently supported streams are `events` and `typing`.

To enable this, the worker must have a HTTP replication listener configured,
have a `worker_name` and be listed in the `instance_map` config. For example to
move event persistence off to a dedicated worker, the shared configuration would
include:

```yaml
instance_map:
    event_persister1:
        host: localhost
        port: 8034

stream_writers:
    events: event_persister1
```

The `events` stream also experimentally supports having multiple writers, where
work is sharded between them by room ID. Note that you *must* restart all worker
instances when adding or removing event persisters. An example `stream_writers`
configuration with multiple writers:

```yaml
stream_writers:
    events:
        - event_persister1
        - event_persister2
```

#### Background tasks

There is also *experimental* support for moving background tasks to a separate
worker. Background tasks are run periodically or started via replication. Exactly
which tasks are configured to run depends on your Chat server configuration (e.g. if
stats is enabled).

To enable this, the worker must have a `worker_name` and can be configured to run
background tasks. For example, to move background tasks to a dedicated worker,
the shared configuration would include:

```yaml
run_background_tasks_on: background_worker
```

You might also wish to investigate the `update_user_directory` and
`media_instance_running_background_jobs` settings.

### `chat.app.pusher`

Handles sending push notifications to sygnal and email. Doesn't handle any
REST endpoints itself, but you should set `start_pushers: False` in the
shared configuration file to stop the main chat sending push notifications.

To run multiple instances at once the `pusher_instances` option should list all
pusher instances by their worker name, e.g.:

```yaml
pusher_instances:
    - pusher_worker1
    - pusher_worker2
```


### `chat.app.appservice`

Handles sending output traffic to Application Services. Doesn't handle any
REST endpoints itself, but you should set `notify_appservices: False` in the
shared configuration file to stop the main chat sending appservice notifications.

Note this worker cannot be load-balanced: only one instance should be active.


### `chat.app.federation_sender`

Handles sending federation traffic to other servers. Doesn't handle any
REST endpoints itself, but you should set `send_federation: False` in the
shared configuration file to stop the main chat sending this traffic.

If running multiple federation senders then you must list each
instance in the `federation_sender_instances` option by their `worker_name`.
All instances must be stopped and started when adding or removing instances.
For example:

```yaml
federation_sender_instances:
    - federation_sender1
    - federation_sender2
```

### `chat.app.media_repository`

Handles the media repository. It can handle all endpoints starting with:

    /chat/media/

... and the following regular expressions matching media-specific administration APIs:

    ^/_chat/admin/v1/purge_media_cache$
    ^/_chat/admin/v1/room/.*/media.*$
    ^/_chat/admin/v1/user/.*/media.*$
    ^/_chat/admin/v1/media/.*$
    ^/_chat/admin/v1/quarantine_media/.*$
    ^/_chat/admin/v1/users/.*/media$

You should also set `enable_media_repo: False` in the shared configuration
file to stop the main chat running background jobs related to managing the
media repository. Note that doing so will prevent the main process from being
able to handle the above endpoints.

In the `media_repository` worker configuration file, configure the http listener to
expose the `media` resource. For example:

```yaml
    worker_listeners:
     - type: http
       port: 8085
       resources:
         - names:
           - media
```

Note that if running multiple media repositories they must be on the same server
and you must configure a single instance to run the background tasks, e.g.:

```yaml
    media_instance_running_background_jobs: "media-repository-1"
```

Note that if a reverse proxy is used , then `/chat/media/` must be routed for both inbound client and federation requests (if they are handled separately).

### `chat.app.user_dir`

Handles searches in the user directory. It can handle REST endpoints matching
the following regular expressions:

    ^/chat/client/(api/v1|r0|unstable)/user_directory/search$

When using this worker you must also set `update_user_directory: False` in the
shared configuration file to stop the main chat running background
jobs related to updating the user directory.

### `chat.app.frontend_proxy`

Proxies some frequently-requested client endpoints to add caching and remove
load from the main chat. It can handle REST endpoints matching the following
regular expressions:

    ^/chat/client/(api/v1|r0|unstable)/keys/upload

If `use_presence` is False in the homeserver config, it can also handle REST
endpoints matching the following regular expressions:

    ^/chat/client/(api/v1|r0|unstable)/presence/[^/]+/status

This "stub" presence handler will pass through `GET` request but make the
`PUT` effectively a no-op.

It will proxy any requests it cannot handle to the main chat instance. It
must therefore be configured with the location of the main instance, via
the `worker_main_http_uri` setting in the `frontend_proxy` worker configuration
file. For example:

    worker_main_http_uri: http://127.0.0.1:8080

### Historical apps

*Note:* Historically there used to be more apps, however they have been
amalgamated into a single `chat.app.generic_worker` app. The remaining apps
are ones that do specific processing unrelated to requests, e.g. the `pusher`
that handles sending out push notifications for new events. The intention is for
all these to be folded into the `generic_worker` app and to use config to define
which processes handle the various proccessing such as push notifications.


## Migration from old config

There are two main independent changes that have been made: introducing Redis
support and merging apps into `chat.app.generic_worker`. Both these changes
are backwards compatible and so no changes to the config are required, however
server admins are encouraged to plan to migrate to Redis as the old style direct
TCP replication config is deprecated.

To migrate to Redis add the `redis` config as above, and optionally remove the
TCP `replication` listener from master and `worker_replication_port` from worker
config.

To migrate apps to use `chat.app.generic_worker` simply update the
`worker_app` option in the worker configs, and where worker are started (e.g.
in systemd service files, but not required for synctl).


## Architectural diagram

The following shows an example setup using Redis and a reverse proxy:

```txt
                     Clients & Federation
                              |
                              v
                        +-----------+
                        |           |
                        |  Reverse  |
                        |  Proxy    |
                        |           |
                        +-----------+
                            | | |
                            | | | HTTP requests
        +-------------------+ | +-----------+
        |                 +---+             |
        |                 |                 |
        v                 v                 v
+--------------+  +--------------+  +--------------+  +--------------+
|   Main       |  |   Generic    |  |   Generic    |  |  Event       |
|   Process    |  |   Worker 1   |  |   Worker 2   |  |  Persister   |
+--------------+  +--------------+  +--------------+  +--------------+
      ^    ^          |   ^   |         |   ^   |          ^    ^
      |    |          |   |   |         |   |   |          |    |
      |    |          |   |   |  HTTP   |   |   |          |    |
      |    +----------+<--|---|---------+   |   |          |    |
      |                   |   +-------------|-->+----------+    |
      |                   |                 |                   |
      |                   |                 |                   |
      v                   v                 v                   v
====================================================================
                                                         Redis pub/sub channel
```
