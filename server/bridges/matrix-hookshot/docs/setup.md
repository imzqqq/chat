Getting set up
==============

This page explains how to set up Hookshot for use with a Matrix homeserver.

## Requirements

Hookshot is fairly light on resources, and can run in as low as 100MB or so of memory.
Hookshot memory requirements may increase depending on the traffic and the number of rooms bridged.

You **must** have administrative access to an existing homeserver in order to set up Hookshot, as
Hookshot requires the homeserver to be configured with its appservice registration.


## Local installation 

This bridge requires at least Node 14 (though 16 is preferred), and Rust installed.

To install Node.JS, [nvm](https://github.com/nvm-sh/nvm) is a good option.

To install Rust, [rustup](https://rustup.rs/) is the preferred solution to stay up to date.

To clone and install, run:

```bash
git clone git@github.com:matrix-org/matrix-hookshot.git
cd matrix-hookshot
yarn # or npm i
```

Starting the bridge (after configuring it), is a matter of running `yarn start`.

## Installation via Docker

To get started quickly, you can use the Docker image [`halfshot/matrix-hookshot`](https://hub.docker.com/r/halfshot/matrix-hookshot).

```bash
docker run \
    --name matrix-hookshot \
    -d \
    -p 9993:9993 \ # Homeserver port
    -p 9000:9000 \ # Webhook port
    -p 9002:9002 \ # Metrics port
    -v /etc/matrix-hookshot:/data \
    halfshot/matrix-hookshot:latest
```

Where `/etc/matrix-hookshot` would contain the configuration files `config.yml` and `registration.yml`. The `passKey` file should also be stored alongside these files. In your config, you should use the path `/data/passkey.pem`.


## Configuration

Copy the `config.sample.yml` to a new file `config.yml`. The sample config is also hosted
[here](./setup/sample-configuration.md) for your convienence.

You should read and fill this in as the bridge will not start without a complete config.

You may validate your config without starting the service by running `yarn validate-config`.
For Docker you can run `docker run --rm -v /absolute-path-to/config.yml:/config.yml halfshot/matrix-hookshot node Config/Config.js /config.yml`

Copy `registration.sample.yml` into `registration.yml` and fill in:
- At a minimum, you will need to replace the `as_token` and `hs_token` and change the domain part of the namespaces.

You will need to link the registration file to the homeserver. Consult your homeserver documentation
on how to add appservices. [Synapse documents the process here](https://matrix-org.github.io/synapse/latest/application_services.html).
### Homeserver Configuration

In addition to providing the registration file above, you also need to tell Hookshot how to reach the homeserver which is hosting it. For clarity, hookshot expects to be able to connect to an existing homeserver which has the Hookshot registration file configured.

```yaml
bridge:
  domain: example.com # The homeserver's server name.
  url: http://localhost:8008 # The URL where Hookshot can reach the client-server API.
  mediaUrl: https://example.com # Optional. The url where media hosted on the homeserver is reachable (this should be publically reachable from the internet)
  port: 9993 # The port where hookshot will listen for appservice requests.
  bindAddress: 127.0.0.1 # The address which Hookshot will bind to. Docker users should set this to `0.0.0.0`.
```

The `port` and `bindAddress` must not conflict with the other listeners in the bridge config. This listeners should **not** be reachable
over the internet to users, as it's intended to be used by the homeserver exclusively. This service listens on `/_matrix/app/`.


### Permissions

The bridge supports fine grained permission control over what services a user can access.
By default, any user on the bridge's own homeserver has full permission to use it.

```yaml
permissions:
  - actor: example.com
    services:
      - service: "*"
        level: admin
```

You must configure a set of "actors" with access to services. An `actor` can be:
- A MxID (also known as a User ID) e.g. `@Half-Shot:half-shot.uk`
- A homeserver domain e.g. `matrix.org`
- A roomId. This will allow any member of this room to complete actions. e.g. `!TlZdPIYrhwNvXlBiEk:half-shot.uk`
- `*`, to match all users.

Each permission set can have a services. The `service` field can be:
- `github`
- `gitlab`
- `jira`
- `feed`
- `figma`
- `webhooks`
- `*`, for any service.

The `level` can be:
 - `commands` Can run commands within connected rooms, but NOT log in to the bridge.
 - `login` All the above, and can also log in to the bridge.
 - `notifications` All the above, and can also bridge their notifications.
 - `manageConnections` All the above, and can create and delete connections (either via the provisioner, setup commands, or state events).
 - `admin` All permissions. Currently, there are no admin features so this exists as a placeholder.

When permissions are checked, if a user matches any of the permission set and one
of those grants the right level for a service, they are allowed access. If none of the 
definitions match, they are denined.

#### Example

A typical setup might be.

```yaml
permissions:
  # Allo all users to send commands to existing services
  - actor: *
    services:
      - service: *
        level: commands
  # Allow any user that is part of this space to manage github connections
  - actor: !TlZdPIYrhwNvXlBiEk:half-shot.uk
    services:
      - service: github
        level: manageConnections
  # Allow users on this domain to log in to jira and github.
  - actor: support.example.com
    services:
      - service: jira
        level: login
      - service: github
        level: commands
  # Allow users on this domain to enable notifications on any service.
  - actor: engineering.example.com
    services:
      - service: *
        level: notifications
  # Allow users on this domain to create connections.
  - actor: management.example.com
    services:
      - service: *
        level: manageConnections
  # Allow this specific user to do any action
  - actor: @alice:example.com
    services:
      - service: *
        level: admin
```

### Listeners configuration

You will need to configure some listeners to make the bridge functional.

```yaml
listeners:
  # (Optional) HTTP Listener configuration.
  # Bind resource endpoints to ports and addresses.
  # 'resources' may be any of webhooks, widgets, metrics, provisioning
  #
  - port: 9000
    bindAddress: 0.0.0.0
    resources:
      - webhooks
  - port: 9001
    bindAddress: 127.0.0.1
    resources:
      - metrics
      - provisioning
```

At a minimum, you should bind the `webhooks` resource to a port and address. You can have multiple resources on the same
port, or one on each. Each listener MUST listen on a unique port.

You will also need to make this port accessible to the internet so services like GitHub can reach the bridge. It
is recommended to factor hookshot into your load balancer configuration, but currrently this process is left as an
excercise to the user.

In terms of API endpoints:

- The `webhooks` resource handles resources under `/`, so it should be on its own listener.
  Note that OAuth requests also go through this listener.
- The `metrics` resource handles resources under `/metrics`.
- The `provisioning` resource handles resources under `/v1/...`.
- The `widgets` resource handles resources under `/widgetapi/v1...`. This may only be bound to **one** listener at present.

<section class="notice">
Please note that the appservice HTTP listener is configured <strong>seperately</strong> from the rest of the bridge (in the `homeserver` section) due to lack of support
in the upstream library. See <a href="https://github.com/turt2live/matrix-bot-sdk/issues/191">this issue</a> for details.
</section>


### Services configuration

You will need to configure some services. Each service has its own documentation file inside the setup subdirectory.

- [Feeds](./setup/feeds.md)
- [Figma](./setup/figma.md)
- [GitHub](./setup/github.md)
- [GitLab](./setup/gitlab.md)
- [Jira](./setup/jira.md)
- [Webhooks](./setup/webhooks.md)

### Logging

The bridge supports some basic logging options. The section is optional, and by default will log at an `info` level.

```yaml
logging:
  # Level of information to report to the logs. Can be `debug`, `info`, `warn` or `error.
  level: info
  # Should the logs output in human-readable format or JSON. If you are using a third-party ingestion service like logstash, use this.
  json: false
  # Ignored if `json` is enabled. Should the logs print the levels in color. This will print extra characters around the logs which may not be suitable for some systems.
  colorize: true
  #  Ignored if `json` is enabled. The timestamp format to use in log lines. See https://github.com/taylorhakes/fecha#formatting-tokens for help on formatting tokens.
  timestampFormat: HH:mm:ss:SSS
```
