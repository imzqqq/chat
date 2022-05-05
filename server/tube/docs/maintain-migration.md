# Instance migration

This guide describes how to migrate your Tube instance from one server to
another. It assumes you have root access to both servers. The process takes
some time - so you may want a caffeinated beverage of your choice - and there
will be a short period of downtime between when you shut down the old server
and point DNS at the new one.

!> **Note:** do not modify anything on the old server until you have successfully migrated the instance.

## Basic steps

1. Setup a new Tube server using the [production guide](https://github.com/Chocobozzz/Tube/blob/develop/support/doc/production.md)
2. Stop Tube on the old server
3. Dump and load the PostgreSQL database using the instructions below
4. Copy or synchronize the `storage/` files using the instructions below (¹)
5. Start Tube on the new server
6. Update your DNS settings to point to the new server
7. Update or copy your Nginx configuration, re-run Let's Encrypt as necessary (²)
8. Enjoy your new Tube server!

## Detailed steps

### What data needs to be migrated

At a high level, you will need to copy over the following:

- The `/var/www/tube/storage` directory, which contains videos, thumbnails, previews, and so on
- The `/var/www/tube/config` file, which contains the configuration
- The PostgreSQL database (using [pg_dump](https://www.postgresql.org/docs/13/static/backup-dump.html))

You might also want to copy the following configuration items, if the new server is configured similarly to the old one:

- The tube-specific nginx configuration (by default found at `/etc/nginx/sites-available/tube`)
- The systemd config files or startup scripts, which may contain tweaks and customizations specific to your server

### Dump and load PostgreSQL

Once you have stopped your Tube instance, run the following command as the `tube`
user on the old server, to generate a text dump of the database:

```bash
sudo -u tube pg_dump -Fc peertube_prod > /tmp/peertube_prod-dump.db
```

Copy the `/tmp/peertube_prod-dump.db` file over to the new server, using `scp` or `rsync` or any similar
file-copying tool:

```bash
scp /tmp/peertube_prod-dump.db user@new.server:/tmp
```

Then on the new system, run:

```bash
sudo -u postgres pg_restore -c -C -d postgres /tmp/peertube_prod-dump.db
```

to restore the database on the new server to the same state as on the old one.

You might see warnings that you can [safely ignore](https://confluence.atlassian.com/bamkb/errors-or-warnings-appear-when-importing-postgres-database-dump-829036698.html).

### Copy `storage/` files

This will probably take a long time! We recommend using `rsync` to avoid
unnecesary copies, but `scp` or any other file-copying utility is fine too.

On your old machine, as the `tube` user, run:

```bash
sudo -u tube rsync -avz ~/storage/ tube@example.com:~/storage/
```
or

```bash
sudo -u tube scp -r ~/storage tube@example.com:~/storage
```

You will need to re-run this if any of the files on the old server change. That's
why it's better to use `rsync`.

(¹) To minimize downtime, you may want to finish an `rsync` run while the old server is still
running, and then re-synchronize after shutting down Tube to capture the last few changes. It's
also possible to avoid this copying step by configuring Tube's `storage` directory to live
on an external storage drive, and then just moving the external storage drive and configuring its mount
point in `fstab`.

This is a good time to copy over any configuration files you wish to use on the
new server, such as `config/`, or the nginx/systemd configuration files or startup
scripts.

#### (²) Let's Encrypt

It's a good idea to copy the Let's Encrypt certs over from the old server
instead of requesting a new cert. Requesting new certs takes longer, and might
fail for some reason, so it's better to avoid that additional source of complication
during the migration process.

### During the migration

You may want to configure nginx to send a 503 (_service unavailable_)
instead of a 501 (_bad gateaway_), and answer with a custom `500.html`.

You will probably want to set the DNS TTL to the lowest possible value about a
day in advance. Set it to a few minutes if you can. This ensures the DNS update
propagates as quickly as possible once you point it to the new IP address,
resulting in less downtime.

### After the migration

You can check [whatsmydns.net](https://www.whatsmydns.net/) to see the progress
of DNS propagation. To jumpstart the process, you can always edit your own
`/etc/hosts` file to point to your new server so you can start playing around
with it early and check if all is all right.

If everything is alright, you can safely shut down the old server.

## Acknowledgements

- Thanks to the [Mastodon](https://joinmastodon.org/) team for their migration guide, which largely inspired this guide.
- Thanks to [@Nutomic](https://framagit.org/Nutomic) for his comments.
