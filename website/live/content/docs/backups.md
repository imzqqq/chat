---
title: "Backups"
description: "Live makes period backups of your data that can be restored."
menu:
  docs:
    parent: "guides"
weight: 1100
toc: true
---

Live will create a backup of your data periodically. It can be found in your `backup` directory as `owncastdb.bak`. You can add this to your normal system backups to keep your Live data safe.

{{<versionsupport feature="Data backup" version="0.0.6">}}

## Restore

Restoring an Live backup file will bring you back to the time the backup was created. This is useful if you want to move data to another machine, want to go back in time for some reason, or there's some type of problem you're looking to resolve.

1. Stop Live from running.
1. Run `./owncast --restoreDatabase <backupfile>`
1. Restart Live as you normally would. It will be using the restored data.

{{<versionsupport feature="Data restore" version="0.0.6">}}
