//! Seshat - a full text search library for Chat clients.
//!
//! There are two modes of operation for Seshat, adding live events as they
//! come in:
//!
//! ```
//! use seshat::{Database, Event, Profile};
//! use tempfile::tempdir;
//!
//! let tmpdir = tempdir().unwrap();
//! let mut database = Database::new(tmpdir.path()).unwrap();
//!
//! /// Method to call for every live event that gets received during a sync.
//! fn add_live_event(event: Event, profile: Profile, database: &Database) {
//!     database.add_event(event, profile);
//! }
//! /// Method to call on every successful sync after live events were added.
//! fn on_sync(database: &mut Database) {
//!     database.commit().unwrap();
//! }
//! ```
//!
//! The other mode is to add events from the room history using the
//! `/room/{room_id}/messages` Chat API endpoint. This method supports
//! storing checkpoints which remember the arguments to continue fetching events
//! from the `/room/{room_id}/messages` API:
//!
//! ```noexecute
//! database.add_historic_events(events, old_checkpoint, new_checkpoint)?;
//! ```
//!
//! Once events have been added a search can be done:
//! ```noexecute
//! let result = database.search("test", &SearchConfig::new()).unwrap();
//! ```

#![deny(missing_docs)]

#[cfg(test)]
#[macro_use]
extern crate lazy_static;

#[macro_use]
extern crate serde;

mod config;
mod database;
mod error;
mod events;
mod index;

pub use database::{
    Connection, Database, DatabaseStats, RecoveryDatabase, RecoveryInfo, SearchBatch, SearchResult,
    Searcher,
};

pub use error::{Error, Result};

pub use config::{Config, Language, LoadConfig, LoadDirection, SearchConfig};
pub use events::{CheckpointDirection, CrawlerCheckpoint, Event, EventType, Profile};

pub use std::sync::mpsc::Receiver;

#[cfg(test)]
pub use events::{EVENT, EVENT_SOURCE, TOPIC_EVENT, TOPIC_EVENT_SOURCE};
