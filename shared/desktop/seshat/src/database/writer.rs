use r2d2_sqlite::SqliteConnectionManager;

use crate::{
    error::Result,
    events::{CrawlerCheckpoint, Event, EventId, Profile},
    index::Writer as IndexWriter,
    Database,
};

pub(crate) struct Writer {
    inner: IndexWriter,
    connection: r2d2::PooledConnection<SqliteConnectionManager>,
    events: Vec<(Event, Profile)>,
    uncommitted_events: Vec<i64>,
    pending_deletion_events: Vec<EventId>,
}

impl Writer {
    pub fn new(
        connection: r2d2::PooledConnection<SqliteConnectionManager>,
        index_writer: IndexWriter,
    ) -> Self {
        Writer {
            inner: index_writer,
            connection,
            events: Vec::new(),
            uncommitted_events: Vec::new(),
            pending_deletion_events: Vec::new(),
        }
    }

    pub fn add_event(&mut self, event: Event, profile: Profile) {
        self.events.push((event, profile));
    }

    pub fn delete_event(&mut self, event_id: EventId) -> Result<bool> {
        Database::delete_event_helper(
            &mut self.connection,
            &mut self.inner,
            event_id,
            &mut self.pending_deletion_events,
        )
    }

    fn mark_events_as_deleted(&mut self) -> Result<()> {
        if self.pending_deletion_events.is_empty() {
            return Ok(());
        }
        Database::mark_events_as_deleted(&mut self.connection, &mut self.pending_deletion_events)
    }

    pub fn write_queued_events(&mut self, force_commit: bool) -> Result<()> {
        let (_, committed) = Database::write_events(
            &mut self.connection,
            &mut self.inner,
            (None, None, &mut self.events),
            force_commit,
            &mut self.uncommitted_events,
        )?;

        if committed {
            self.mark_events_as_deleted()?;
        }

        Ok(())
    }

    pub fn write_historic_events(
        &mut self,
        checkpoint: Option<CrawlerCheckpoint>,
        old_checkpoint: Option<CrawlerCheckpoint>,
        mut events: Vec<(Event, Profile)>,
        force_commit: bool,
    ) -> Result<bool> {
        let empty_events = events.is_empty();
        let (ret, committed) = Database::write_events(
            &mut self.connection,
            &mut self.inner,
            (checkpoint, old_checkpoint, &mut events),
            force_commit,
            &mut self.uncommitted_events,
        )?;

        if committed {
            self.mark_events_as_deleted()?;
        }

        if empty_events {
            Ok(false)
        } else {
            Ok(ret)
        }
    }

    pub fn load_unprocessed_events(&mut self) -> Result<()> {
        let mut ret = Database::load_uncommitted_events(&self.connection)?;

        for (id, event) in ret.drain(..) {
            self.uncommitted_events.push(id);
            self.inner.add_event(&event);
        }

        let ret = Database::load_pending_deletion_events(&self.connection)?;

        for event_id in &ret {
            self.inner.delete_event(&event_id);
        }

        self.pending_deletion_events.extend(ret);

        Ok(())
    }

    pub fn shutdown(self) -> Result<()> {
        self.inner.wait_merging_threads()?;
        Ok(())
    }
}
