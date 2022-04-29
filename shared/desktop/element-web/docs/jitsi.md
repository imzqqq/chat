# Meet in Chat

Chat uses [Meet](https://jitsi.org/) for conference calls, which provides options for
self-hosting your own server and supports most major platforms.

1:1 calls, or calls between you and one other person, do not use Meet. Instead, those
calls work directly between clients or via TURN servers configured on the respective
homeservers.

There's a number of ways to start a Meet call: the easiest way is to click on the
voice or video buttons near the message composer in a room with more than 2 people. This
will add a Meet widget which allows anyone in the room to join.

Integration managers (available through the 4 squares in the top right of the room) may
provide their own approaches for adding Meet widgets.

## Configuring Chat to use your self-hosted Meet server

Chat will use the Meet server that is embedded in the widget, even if it is not the
one you configured. This is because conference calls must be held on a single Meet
server and cannot be split over multiple servers.

However, you can configure Chat to *start* a conference with your Meet server by adding
to your [config](./config.md) the following:

```json
{
  "jitsi": {
    "preferredDomain": "your.jitsi.example.org"
  }
}
```

The default is `jitsi.riot.im` (a free service offered by Chat), and the demo site for
Meet uses `meet.jit.si` (also free).

Once you've applied the config change, refresh Chat and press the call button. This
should start a new conference on your Meet server.

**Note**: The widget URL will point to a `jitsi.html` page hosted by Chat. The Meet
domain will appear later in the URL as a configuration parameter.

**Hint**: If you want everyone on your homeserver to use the same Meet server by
default, and you are using element-web 1.6 or newer, set the following on your homeserver's
`/.well-known/matrix/client` config:

```json
{
  "im.vector.riot.jitsi": {
    "preferredDomain": "your.jitsi.example.org"
  }
}
```

## Chat Android

Chat Android (1.0.5+) supports custom Meet domains, similar to Chat Web above.

1:1 calls, or calls between you and one other person, do not use Meet. Instead, those
calls work directly between clients or via TURN servers configured on the respective
homeservers.

For rooms with more than 2 joined members, when creating a Meet conference via call/video buttons of the toolbar (not via integration manager), Chat Android will create a widget using the [wrapper](./jitsi-dev.md) hosted on `apps.chat.imzqqq.top`.
The domain used is the one specified by the `/.well-known/matrix/client` endpoint, and if not present it uses the fallback defined in `config.xml` (jitsi.riot.im)

For active Meet widgets in the room, a native Meet widget UI is created and points to the instance specified in the `domain` key of the widget content data.

Chat Android manages allowed native widgets permissions a bit differently than web widgets (as the data shared are different and never shared with the widget URL). For Meet widgets, permissions are requested only once per domain (consent saved in account data).

## Chat iOS

Currently the Chat mobile apps do not support custom Meet servers and will instead
use the default `jitsi.riot.im` server. When users on the mobile apps join the call,
they will be joining a different conference which has the same name, but not the same
participants. This is a known bug and which needs to be fixed.
