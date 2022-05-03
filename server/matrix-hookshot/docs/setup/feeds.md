# Feeds

You can configure hookshot to bridge RSS/Atom feeds into Matrix.

## Configuration

```yaml
feeds:
  # (Optional) Configure this to enable RSS/Atom feed support
  #
  enabled: true
  pollIntervalSeconds: 600
```

`pollIntervalSeconds` specifies how often each feed will be checked for updates.
It may be checked less often if under exceptional load, but it will never be checked more often than every `pollIntervalSeconds`.

Each feed will only be checked once, regardless of the number of rooms to which it's bridged.

No entries will be bridged upon the “initial sync” -- all entries that exist at the moment of setup will be considered to be already seen.

## Usage

### Adding new feeds

To add a feed to your room:

  - Invite the bot user to the room.
  - Make sure the bot able to send state events (usually the Moderator power level in clients)
  - Say `!hookshot feed <URL>` where `<URL>` links to an RSS/Atom feed you want to subscribe to.

### Listing feeds

You can list all feeds that a room you're in is currently subscribed to with `!hookshot feed list`.
It requires no special permissions from the user issuing the command.

### Removing feeds

To remove a feed from a room, say `!hookshot feed remove <URL>`, with the URL specifying which feed you want to unsubscribe from.
