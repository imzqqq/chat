# Some random useful commands
The console right now lacks comprehensive help; here are some useful commands seen in the wild.
You can use the console (*CTRL-C*) or the *!control:SERVERNAME* room. Riot is super inconvenient for the latter, but *nheko* (for example) makes it convenient to edit the previous line.

Most of these commands are only interesting throughout the betatesting, like unscrewing room or server states, or examining what's going on. 

## Table of Contents
1. [rooom](#room)
2. [rooms](#rooms)
3. [conf](#conf)
4. [log](#log)
5. [fed](#fed)
6. [db](#db)
7. [user](#user)
8. [redact](#redact)
9. [mod](#mod)

## MXID-as-command
A Matrix-ID itself is recognized as a command for convenience.

### `<room_id>`
Shows the known data of the specific room_id.

### `<room_id> <type> <state_key>`
Shows the known data of an event in the room's state.

### `<event_id>`
Shows the known data of the specific event_id.

### `<event_id> content`
Shows the content field of the specific event_id.

### `<event_id> raw`
Shows the raw JSON of the specific event_id.

### `<room_alias>`
Room aliases are resolved to a `room_id` throughout all console commands. In this case it behaves as `<room_id>` was described earlier.

***

<a name=room/>

## room
<room> can be
* room_id (`!kjkjkjljlkjkljlk:example.com`)
* alias (`#alias:example.com`)

### `room events <room>`
lookup room events

### `room head reset <room>`
Rebootstrap room: try to clear and re-retrieve all data from the start

### `room restrap <join-event_id> <member_server>`
Re-bootstrap a room from a known join event, using any server which has the said event. Can be usd to rebuild the room graph in case of... need.

### `room get <room> [<event_type> [<mxid>]]`
List room events, optionally restricting to the given type or the given mxid. Maybe any of them.

### `room state <room>`
Get complete room state according to the server

### `room state rebuild <room>`
Manually rebuild the _present_ `room state` from the `room state space`. Required if the present state resolves incorrectly or falls out of sync due to bugs or missing functionality in the server.

### `room state space rebuild <room>`
Manually rebuild the _higher dimension_ `room state space` table from the `room events` table. Required if any state event is missing from this table, but is in the `room events` (rare).

### `room state space <room> [event_type] [state_key]` 
Show all possible states for a given `(type,state_key)` "cell" 
 - The state which is selected for the present-state is marked with an `*` asterisk. 
 - A state which fails one of the three authentication phases is marked with an `X` and a reason string may be present on the right. 
 - A state which has been redacted is marked with an `R` 

### `room members <room>`
Show members of a room according to the server.

### `room events <room>`
Show room events (or at least the first few ones).

### `room events <room> <int> <int>`
Show events only ? to ?. The first number is ? and the second is ?.

### `room purge <room>`
Remove room data from server [will be backfilled if needed]. May be needed to remove <s>histerical</s> historical rooms causing trouble.

<a name="rooms"/>

## rooms

### `rooms head reset remote_joined_only`
Mass reset room heads of remote rooms

***

<a name="conf"/>

## conf
Configuration and settings.

### `conf set ircd.log.info.file.enable true`
Enable logging to file(s).

### `conf diff`
Show all differences between your current configuration and the defaults.

***

<a name="log"/>

## log
Log level and facility control.

### `log`
List all logging facilities. `CONSOLE` and `FILE` are displayed if active.

### `log level [level-name]`
Enable log levels equal and less than provided level (affects console only). Most severe log level is CRITICAL (0) and least severe level is DEBUG (7). Therefor `log level CRITICAL` only displays CRITICAL messages.

### `log mask [facility] [...]`
Only enables facilities listed (affects console only). All other facilities are muted.
Note: `log mask` with no arguments *mutes all* facilities.

### `log unmask [facility] [...]`
Mutes all facilities listed (affects console only). All other facilities will be enabled.

Example: `log unmask m.presence m.typing m.receipt`

Note: `log unmask` with no arguments *unmutes all* facilities.

***

<a name="fed"/>

## fed        
Federation related.

### `fed state <room> <server_in_room> eval`
Re-fetch room states through federation

***

<a name="db"/>

## db

### `db`
List active databases

### `db info <db>`
Information on a database (`events`, `media`).

### `db columns <db>`
Show database columns (and their size).

### `db compact <db> <table>`
Call a *RocksDB* compaction on *db* (`events` or `media`) *table* (like `_room_head`).

### `db cache events *`
Majority of the memory usage is db cache. This command shows the memory usage of the db cache. If you use two stars (`**`) you get the details. 
You can tailor the "Capacity" by configuring like `conf ircd.m.dbs.content.cache.size`.

***

<a name="user"/>

## user

### `user room_tags <mxid>`
Show room tags of an user. (Supposedly.)

***

<a name="redact"/>

## redact

### `redact <event_id> [user_id]` or `redact <room> <type> <state_key> [user_id]`
Quick redact command. The user_id defaults to `@ircd:your.host` which is only useful for internal rooms. For
public rooms you must give the user with permission to redact.

Some examples:
- `redact !dns:your.host well-known.matrix.server some.host` - drop record from the well-known cache.
- `redact !dns:your.host ircd.net.dns.rrs.AAAA some.host` - drop ip6 record from the DNS cache.

***

<a name="mod"/>

## mod

### `mod reload <module>`
This is useful when you want to restart a module without `die`ing the whole server. Like, when you update `webbapp` you out to
* `mod reload web_root`
since The Construct have cached the webapp files.
