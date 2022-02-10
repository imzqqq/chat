# Setting up Chat server with Workers and Systemd

This is a setup for managing chat with systemd, including support for
managing workers. It provides a `chat-server` service for the master, as
well as a `chat-server-worker@` service template for any workers you
require. Additionally, to group the required services, it sets up a
`chat-server.target`.

See the folder [system](https://github.com/matrix-org/chat/tree/develop/docs/systemd-with-workers/system/)
for the systemd unit files.

The folder [workers](https://github.com/matrix-org/chat/tree/develop/docs/systemd-with-workers/workers/)
contains an example configuration for the `federation_reader` worker.

## Chat server configuration files

See [the worker documentation](../workers.md) for information on how to set up the
configuration files and reverse-proxy correctly.
Below is a sample `federation_reader` worker configuration file.
```yaml
{{#include workers/federation_reader.yaml}}
```

Systemd manages daemonization itself, so ensure that none of the configuration
files set either `daemonize` or `worker_daemonize`.

The config files of all workers are expected to be located in
`/etc/chat-server/workers`. If you want to use a different location, edit
the provided `*.service` files accordingly.

There is no need for a separate configuration file for the master process.

## Set up

1. Adjust chat configuration files as above.
1. Copy the `*.service` and `*.target` files in [system](https://github.com/matrix-org/chat/tree/develop/docs/systemd-with-workers/system/)
to `/etc/systemd/system`.
1. Run `systemctl daemon-reload` to tell systemd to load the new unit files.
1. Run `systemctl enable chat-server.service`. This will configure the
chat master process to be started as part of the `chat-server.target`
target.
1. For each worker process to be enabled, run `systemctl enable
chat-server-worker@<worker_name>.service`. For each `<worker_name>`, there
should be a corresponding configuration file.
`/etc/chat-server/workers/<worker_name>.yaml`.
1. Start all the chat processes with `systemctl start chat-server.target`.
1. Tell systemd to start chat on boot with `systemctl enable chat-server.target`.

## Usage

Once the services are correctly set up, you can use the following commands
to manage your chat installation:

```sh
# If we just created the python .env environment, we need to manually install it
/home/imzqqq/workspace/chat/server/chat/python/.env/bin/python3 -m pip  install -e canonicaljson
/home/imzqqq/workspace/chat/server/chat/python/.env/bin/python3 -m pip  install -e chat-ldap3
/home/imzqqq/workspace/chat/server/chat/python/.env/bin/python3 -m pip  install -e ".[all,lint,mypy,test]"

# Restart Chat server master and all workers
systemctl restart chat-server.target

# Stop Chat server and all workers
systemctl stop chat-server.target

# Restart the master alone
systemctl start chat-server.service

# Restart a specific worker (eg. federation_reader); the master is
# unaffected by this.
systemctl restart chat-server-worker@federation_reader.service

# Add a new worker (assuming all configs are set up already)
systemctl enable chat-server-worker@federation_writer.service
systemctl restart chat-server.target
```

## Hardening

**Optional:** If further hardening is desired, the file
`override-hardened.conf` may be copied from
[contrib/systemd/override-hardened.conf](https://github.com/matrix-org/chat/tree/develop/contrib/systemd/)
in this repository to the location
`/etc/systemd/system/chat-server.service.d/override-hardened.conf` (the
directory may have to be created). It enables certain sandboxing features in
systemd to further secure the chat service. You may read the comments to
understand what the override file is doing. The same file will need to be copied to
`/etc/systemd/system/chat-server-worker@.service.d/override-hardened-worker.conf`
(this directory may also have to be created) in order to apply the same
hardening options to any worker processes.

Once these files have been copied to their appropriate locations, simply reload
systemd's manager config files and restart all Chat server services to apply the hardening options. They will automatically
be applied at every restart as long as the override files are present at the
specified locations.

```sh
systemctl daemon-reload

# Restart services
systemctl restart chat-server.target
```

In order to see their effect, you may run `systemd-analyze security
chat-server.service` before and after applying the hardening options to see
the changes being applied at a glance.
