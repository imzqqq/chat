# Production guide

* [Installation](#installation)
* [Upgrade](#upgrade)

## Installation

Please don't install Tube for production on a device behind a low bandwidth connection (example: your ADSL link).
If you want information about the appropriate hardware to run Tube, please see the [FAQ](https://joinpeertube.org/en_US/faq#should-i-have-a-big-server-to-run-tube).

### :hammer: Dependencies

Follow the steps of the [dependencies guide](dependencies.md).

### :construction_worker: Tube user

Create a `tube` user with `/var/www/tube` home:

```bash
sudo useradd -m -d /var/www/tube -s /bin/bash -p tube tube
```

Set its password:

```bash
sudo passwd tube
```

**On FreeBSD**

```bash
sudo pw useradd -n tube -d /var/www/tube -s /usr/local/bin/bash -m
sudo passwd tube
```

or use `adduser` to create it interactively.

### :card_file_box: Database

Create the production database and a tube user inside PostgreSQL:

```bash
cd /var/www/tube
sudo -u postgres createuser -P imzqqq
```

Here you should enter a password for PostgreSQL `imzqqq` user, that should be copied in `production.yaml` file.
Don't just hit enter else it will be empty.

```bash
sudo -u postgres createdb -O imzqqq -E UTF8 -T template0 tube
```

Then enable extensions Tube needs:

```bash
sudo -u postgres psql -c "CREATE EXTENSION pg_trgm;" tube
sudo -u postgres psql -c "CREATE EXTENSION unaccent;" tube
```

### :page_facing_up: Prepare Tube directory

Fetch the latest tagged version of Tube:

```bash
VERSION=$(curl -s https://api.github.com/repos/chocobozzz/tube/releases/latest | grep tag_name | cut -d '"' -f 4) && echo "Latest tube version is $VERSION"
```

Open the tube directory, create a few required directories:

```bash
cd /var/www/tube
sudo -u imzqqq mkdir config storage versions
sudo -u imzqqq chmod 750 config/
```

Download the latest version of the tube client, unzip it and remove the zip:

```bash
cd /var/www/tube/versions
# Releases are also available on https://builds.joinpeertube.org/release
sudo -u tube wget -q "https://github.com/Chocobozzz/Tube/releases/download/${VERSION}/tube-${VERSION}.zip"
sudo -u tube unzip -q tube-${VERSION}.zip && sudo -u tube rm tube-${VERSION}.zip
```

Install tube:

```bash
cd /var/www/tube
sudo -u tube ln -s versions/tube-${VERSION} ./tube-latest
cd ./tube-latest && sudo -H -u tube yarn install --production --pure-lockfile
```

### :wrench: Tube configuration

Copy the default configuration file that contains the default configuration provided by Tube.
You **must not** update this file.

```bash
cd /var/www/tube
sudo -u tube cp tube-latest/config/default.yaml config/default.yaml
```

Now copy the production example configuration:

```bash
cd /var/www/tube
sudo -u tube cp tube-latest/config/production.yaml.example config/production.yaml
```

Then edit the `config/production.yaml` file according to your webserver
and database configuration (`webserver`, `database`, `redis`, `smtp` and `admin.email` sections in particular).
Keys defined in `config/production.yaml` will override keys defined in `config/default.yaml`.

**Tube does not support webserver host change**. Even though [Tube CLI can help you to switch hostname](https://docs.joinpeertube.org/maintain-tools?id=update-hostjs) there's no official support for that since it is a risky operation that might result in unforeseen errors.

### :truck: Webserver

We only provide official configuration files for Nginx.

Copy the nginx configuration template:

```bash
sudo cp /var/www/tube/tube-latest/support/nginx/tube /etc/nginx/sites-available/tube
```

Then set the domain for the webserver configuration file.
Replace `[tube-domain]` with the domain for the tube server.

```bash
sudo sed -i 's/${WEBSERVER_HOST}/[tube-domain]/g' /etc/nginx/sites-available/tube
sudo sed -i 's/${PEERTUBE_HOST}/127.0.0.1:9000/g' /etc/nginx/sites-available/tube
```

Then modify the webserver configuration file. Please pay attention to the `alias` keys of the static locations.
It should correspond to the paths of your storage directories (set in the configuration file inside the `storage` key).

```bash
sudo vim /etc/nginx/sites-available/tube
```

Activate the configuration file:

```bash
sudo ln -s /etc/nginx/sites-available/tube /etc/nginx/sites-enabled/tube
```

To generate the certificate for your domain as required to make https work you can use [Let's Encrypt](https://letsencrypt.org/):

```bash
sudo systemctl stop nginx
sudo certbot certonly --standalone --post-hook "systemctl restart nginx"
sudo systemctl reload nginx
```

Now you have the certificates you can reload nginx:

```bash
sudo systemctl reload nginx
```

Certbot should have installed a cron to automatically renew your certificate.
Since our nginx template supports webroot renewal, we suggest you to update the renewal config file to use the `webroot` authenticator:

```bash
# Replace authenticator = standalone by authenticator = webroot
# Add webroot_path = /var/www/certbot
sudo vim /etc/letsencrypt/renewal/your-domain.com.conf
```

**FreeBSD**
On FreeBSD you can use [Dehydrated](https://dehydrated.io/) `security/dehydrated` for [Let's Encrypt](https://letsencrypt.org/)

```bash
sudo pkg install dehydrated
```

### :alembic: TCP/IP Tuning

**On Linux**

```bash
sudo cp /var/www/tube/tube-latest/support/sysctl.d/30-tube-tcp.conf /etc/sysctl.d/
$ sudo sysctl -p /etc/sysctl.d/30-tube-tcp.conf
```

Your distro may enable this by default, but at least Debian 9 does not, and the default FIFO
scheduler is quite prone to "Buffer Bloat" and extreme latency when dealing with slower client
links as we often encounter in a video server.

### :bricks: systemd

If your OS uses systemd, copy the configuration template:

```bash
sudo cp /var/www/tube/tube-latest/support/systemd/tube.service /etc/systemd/system/
```

Check the service file (Tube paths and security directives):

```bash
sudo vim /etc/systemd/system/tube.service
```

Tell systemd to reload its config:

```bash
sudo systemctl daemon-reload
```

If you want to start Tube on boot:

```bash
sudo systemctl enable tube
```

Run:

```bash
sudo systemctl start tube
sudo journalctl -feu tube
```

**FreeBSD**
On FreeBSD, copy the startup script and update rc.conf:

```bash
sudo install -m 0555 /var/www/tube/tube-latest/support/freebsd/tube /usr/local/etc/rc.d/
sudo sysrc peertube_enable="YES"
```

Run:

```bash
sudo service tube start
```

### :bricks: OpenRC

If your OS uses OpenRC, copy the service script:

```bash
sudo cp /var/www/tube/tube-latest/support/init.d/tube /etc/init.d/
```

If you want to start Tube on boot:

```bash
sudo rc-update add tube default
```

Run and print last logs:

```bash
sudo /etc/init.d/tube start
tail -f /var/log/tube/tube.log
```

### :technologist: Administrator

The administrator password is automatically generated and can be found in the Tube
logs (path defined in `production.yaml`). You can also set another password with:

```bash
cd /var/www/tube/tube-latest && NODE_CONFIG_DIR=/var/www/tube/config NODE_ENV=production npm run reset-password -- -u root
```

Alternatively you can set the environment variable `PT_INITIAL_ROOT_PASSWORD`,
to your own administrator password, although it must be 6 characters or more.

### :tada: What now?

Now your instance is up you can:

* Add your instance to the public Tube instances index if you want to: <https://instances.joinpeertube.org/>
* Check [available CLI tools](/support/doc/tools.md)

## Upgrade

### Tube instance

**Check the changelog (in particular BREAKING CHANGES!):** <https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md>

#### Auto

The password it asks is Tube's database user password.

```bash
cd /var/www/tube/tube-latest/scripts && sudo -H -u tube ./upgrade.sh
sudo systemctl restart tube # Or use your OS command to restart Tube if you don't use systemd
```

#### Manually

Make a SQL backup

```bash
$ SQL_BACKUP_PATH="backup/sql-peertube_prod-$(date -Im).bak" && \
    cd /var/www/tube && sudo -u tube mkdir -p backup && \
    sudo -u postgres pg_dump -F c peertube_prod | sudo -u tube tee "$SQL_BACKUP_PATH" >/dev/null
```

Fetch the latest tagged version of Peertube:

```bash
VERSION=$(curl -s https://api.github.com/repos/chocobozzz/tube/releases/latest | grep tag_name | cut -d '"' -f 4) && echo "Latest Peertube version is $VERSION"
```

Download the new version and unzip it:

```bash
$ cd /var/www/tube/versions && \
    sudo -u tube wget -q "https://github.com/Chocobozzz/Tube/releases/download/${VERSION}/tube-${VERSION}.zip" && \
    sudo -u tube unzip -o tube-${VERSION}.zip && \
    sudo -u tube rm tube-${VERSION}.zip
```

Install node dependencies:

```bash
$ cd /var/www/tube/versions/tube-${VERSION} && \
    sudo -H -u tube yarn install --production --pure-lockfile
```

Copy new configuration defaults values and update your configuration file:

```bash
sudo -u tube cp /var/www/tube/versions/tube-${VERSION}/config/default.yaml /var/www/tube/config/default.yaml
diff /var/www/tube/versions/tube-${VERSION}/config/production.yaml.example /var/www/tube/config/production.yaml
```

Change the link to point to the latest version:

```bash
$ cd /var/www/tube && \
    sudo unlink ./tube-latest && \
    sudo -u tube ln -s versions/tube-${VERSION} ./tube-latest
```

### nginx

Check changes in nginx configuration:

```bash
cd /var/www/tube/versions
diff "$(ls --sort=t | head -2 | tail -1)/support/nginx/tube" "$(ls --sort=t | head -1)/support/nginx/tube"
```

### systemd

Check changes in systemd configuration:

```bash
cd /var/www/tube/versions
diff "$(ls --sort=t | head -2 | tail -1)/support/systemd/tube.service" "$(ls --sort=t | head -1)/support/systemd/tube.service"
```

### Restart Tube

If you changed your nginx configuration:

```bash
sudo systemctl reload nginx
```

If you changed your systemd configuration:

```bash
sudo systemctl daemon-reload
```

Restart Tube and check the logs:

```bash
sudo systemctl restart tube && sudo journalctl -fu tube
```

### Things went wrong?

Change `tube-latest` destination to the previous version and restore your SQL backup:

```bash
$ OLD_VERSION="v0.42.42" && SQL_BACKUP_PATH="backup/sql-peertube_prod-2018-01-19T10:18+01:00.bak" && \
    cd /var/www/tube && sudo -u tube unlink ./tube-latest && \
    sudo -u tube ln -s "versions/tube-$OLD_VERSION" tube-latest && \
    sudo -u postgres pg_restore -c -C -d postgres "$SQL_BACKUP_PATH" && \
    sudo systemctl restart tube
```
