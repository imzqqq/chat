# Seshat

Seshat is a database and indexer for Chat events.

Its main use is to be used as a full text search backend for Chat clients.

## JavaScript bindings

Seshat provides JavaScript bindings which can be found in the
[seshat-node](./seshat-node) subdir.

## Usage

There are two modes of operation for Seshat, adding live events as they
come in:

```rust
use seshat::{Database, Event, Profile};
use tempfile::tempdir;

let tmpdir = tempdir().unwrap();
let mut db = Database::new(tmpdir.path()).unwrap();

/// Method to call for every live event that gets received during a sync.
fn add_live_event(event: Event, profile: Profile, database: &Database) {
    database.add_event(event, profile);
}
/// Method to call on every successful sync after live events were added.
fn on_sync(database: &mut Database) {
    database.commit().unwrap();
}
```

The other mode is to add events from the room history using the
`/room/{room_id}/messages` Chat API endpoint. This method supports
storing checkpoints which remember the arguments to continue fetching events
from the `/room/{room_id}/messages` API:

```rust
database.add_historic_events(events, old_checkpoint, new_checkpoint)?;
```

Once events have been added a search can be done:

```rust
let result = database.search("test", &SearchConfig::new()).unwrap();
```
