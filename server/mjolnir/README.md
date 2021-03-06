# mjolnir

A moderation tool for chat server.

## Features

As an all-in-one moderation tool, it can protect your server from malicious invites, spam
messages, and whatever else you don't want. In addition to server-level protection, Mjolnir
is great for communities wanting to protect their rooms without having to use their personal
accounts for moderation.

The bot by default includes support for bans, redactions, anti-spam, server ACLs, room
directory changes, room alias transfers, account deactivation, room shutdown, and more.

A Synapse module is also available to apply the same rulesets the bot uses across an entire
homeserver.

## Bot configuration

It is recommended to use [Pantalaimon](../pantalaimon/README.md) so your
management room can be encrypted. This also applies if you are looking to moderate an encrypted
room.

If you aren't using encrypted rooms anywhere, get an access token by opening Riot in an
incognito/private window and log in as the bot. From the Help & Support tab in settings there
is an access token field - copy and paste that into your config. Most importantly: do not log
out and instead just close the window. Logging out will make the token you just copied useless.

**Note**: Mjolnir expects to be free of rate limiting - you need the full user name (e.g. @mjolnir:my-homeserver.chat) of Mjolnir's user. Then enter synapse's database table that you configured and run: `insert into ratelimit_override values ('@mjolnir:my-homeserver.chat', 0, 0);`

**Note**: To deactivate users, move aliases, shutdown rooms, etc Mjolnir will need to be a server
admin.

## Docker installation (preferred)

Mjolnir can be built yourself with `docker build -t mjolnir .`.

```bash
# Copy and edit the config. It is not recommended to change the data path.
mkdir -p /etc/mjolnir/config
cp config/default.yaml /etc/mjolnir/config/production.yaml
nano /etc/mjolnir/config/production.yaml

docker run --rm -it -v /etc/mjolnir:/data matrixdotorg/mjolnir:latest
```

## Build it (alternative installation)

This bot requires `yarn` and Node 10.

```bash
yarn install
yarn build

# Copy and edit the config. It *is* recommended to change the data path.
cp config/default.yaml config/development.yaml
#nano config/development.yaml

node lib/index.js
```

## Quickstart guide

After your bot is up and running, you'll want to run a couple commands to get everything
set up:

1. `!mjolnir list create COC code-of-conduct-ban-list` - This will create a new ban list
   with the shortcode `COC` and an alias of `#code-of-conduct-ban-list:example.org`. You
   will be invited to the room it creates automatically where you can change settings such
   as the visibility of the room.
2. `!mjolnir default COC` - This sets the default ban list to the list we just created to
   help with the ban commands later on.
3. Review the [Moderator's Guide](./docs/moderators.md).
4. Review `!mjolnir help` to see what else the bot can do.

## Synapse Module

Using the bot to manage your rooms is great, however if you want to use your ban lists
(or someone else's) on your server to affect all of your users then a Synapse module
is needed. Primarily meant to block invites from undesired homeservers/users, Mjolnir's
Synapse module is a way to interpret ban lists and apply them to your entire homeserver.

First, install the module to your Synapse python environment:

```bash
pip install -e "git+https://github.com/matrix-org/mjolnir.git#egg=mjolnir&subdirectory=synapse_antispam"
```

*Note*: Where your python environment is depends on your installation method.

Then add the following to your `homeserver.yaml`:

```yaml
spam_checker:
  module: mjolnir.AntiSpam
  config:
    # Prevent servers/users in the ban lists from inviting users on this
    # server to rooms. Default true.
    block_invites: true
    # Flag messages sent by servers/users in the ban lists as spam. Currently
    # this means that spammy messages will appear as empty to users. Default
    # false.
    block_messages: false
    # Remove users from the user directory search by filtering matrix IDs and
    # display names by the entries in the user ban list. Default false.
    block_usernames: false
    # The room IDs of the ban lists to honour. Unlike other parts of Mjolnir,
    # this list cannot be room aliases or permalinks. This server is expected
    # to already be joined to the room - Mjolnir will not automatically join
    # these rooms.
    ban_lists:
      - "!roomid:example.org"
```

*Note*: Although this is described as a "spam checker", it does much more than fight
spam.

Be sure to change the configuration to match your setup. Your server is expected to
already be participating in the ban lists - if it is not, you will need to have a user
on your homeserver join. The antispam module will not join the rooms for you.

If you change the configuration, you will need to restart Synapse. You'll also need
to restart Synapse to install the plugin.

## Development

TODO. It's a TypeScript project with a linter.
