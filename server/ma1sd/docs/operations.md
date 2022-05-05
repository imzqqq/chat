# Operations Guide
- [Overview](#overview)
- [Maintenance](#maintenance)
- [Backuo](#backup)

## Overview
This document gives various information for the day-to-day management and operations of ma1sd.

## Maintenance
ma1sd does not require any maintenance task to run at optimal performance.

## Backup
### Run
ma1sd requires all file in its configuration and data directory to be backed up.  
They are usually located at:
- `/etc/ma1sd`
- `/var/lib/ma1sd`

### Restore
Reinstall ma1sd, restore the two folders above in the appropriate location (depending on your install method) and you
will be good to go. Simply start ma1sd to restore functionality.
