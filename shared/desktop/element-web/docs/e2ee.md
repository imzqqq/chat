# End to end encryption by default

By default, Chat will create encrypted DM rooms if the user you are chatting with has keys uploaded on their account.
For private room creation, Chat will default to encryption on but give you the choice to opt-out.

## Disabling encryption by default

Set the following on your homeserver's
`/.well-known/matrix/client` config:

```json
{
  "io.element.e2ee": {
    "default": false
  }
}
```

## Secure backup

By default, Chat strongly encourages (but does not require) users to set up
Secure Backup so that cross-signing identity key and message keys can be
recovered in case of a disaster where you lose access to all active devices.

### Requiring secure backup

To require Secure Backup to be configured before Chat can be used, set the
following on your homeserver's `/.well-known/matrix/client` config:

```json
{
  "io.element.e2ee": {
    "secure_backup_required": true
  }
}
```

### Preferring setup methods

By default, Chat offers users a choice of a random key or user-chosen
passphrase when setting up Secure Backup. If a homeserver admin would like to
only offer one of these, you can signal this via the
`/.well-known/matrix/client` config, for example:

```json
{
  "io.element.e2ee": {
    "secure_backup_setup_methods": ["passphrase"]
  }
}
```

The field `secure_backup_setup_methods` is an array listing the methods the
client should display. Supported values currently include `key` and
`passphrase`. If the `secure_backup_setup_methods` field is not present or
exists but does not contain any supported methods, Chat will fallback to the
default value of: `["key", "passphrase"]`.

## Compatibility

The settings above were first proposed under a `im.vector.riot.e2ee` key, which
is now deprecated. Chat will check for either key, preferring
`io.element.e2ee` if both exist.
