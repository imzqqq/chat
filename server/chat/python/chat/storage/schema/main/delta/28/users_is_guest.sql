ALTER TABLE users ADD is_guest SMALLINT DEFAULT 0 NOT NULL;
/*
 * NB: any guest users created between 27 and 28 will be incorrectly
 * marked as not guests: we don't bother to fill these in correctly
 * because guest access is not really complete in 27 anyway so it's
 * very unlikley there will be any guest users created.
 */
