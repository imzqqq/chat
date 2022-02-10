# Unofficial installations

## Git

!> **Warning** This guide assumes that you have read the instructions in
<a href="https://github.com/Chocobozzz/Tube/blob/develop/support/doc/production.md">production.md</a>
and thus omits some configurations steps (reverse proxy, database configuration, etc.). At
the end you should have a Tube instance running the latest development version, which can
be considered unstable.

First, go to the Peertube folder and switch to the Peertube user.

```bash
cd /var/www/tube/versions/
sudo -u tube -H bash
```

Then clone the git repository.

```bash
git clone https://github.com/Chocobozzz/Tube.git tube-develop
cd tube-develop/
```

It should automatically be on the `develop` branch, which you can verify with `git branch`. You can
also switch to another branch or a specific commit with `git checkout [branch or commit]`. Once you
have the correct version, run the build:

```bash
yarn install --pure-lockfile
npm run build
```

The compilation will take a long time. You can also run it on your local computer, and transfer the
entire folder to your server.

Now you should make sure to add any new config fields to your `production.yaml`. And you should make
a backup of the database:

```bash
SQL_BACKUP_PATH="backup/sql-peertube_prod-$(date -Im).bak" && \
     cd /var/www/tube && sudo -u tube mkdir -p backup && \
     sudo -u postgres pg_dump -F c peertube_prod | sudo -u tube tee "$SQL_BACKUP_PATH" >/dev/null
```

Finally, update the `tube-latest` symlink to point at the new version:

```bash
cd /var/www/tube && \
    sudo unlink ./tube-latest && \
    sudo -u tube ln -s versions/tube-develop ./tube-latest
```

Now you just need to restart Peertube. With systemd, just run `sudo systemctl restart tube`.

Do not try to upgrade from one development version to another by running `git pull` and `npm run build`. This
will break your website. Either switch back to a release version, or make a copy of the `tube-develop`
folder and run the compilation there.

## ArchLinux

On Arch Linux, Tube can be installed through the Arch User Repository thanks to a __community package__ made by [daftaupe](https://aur.archlinux.org/packages/tube/).

```sh
asp checkout tube
cd tube
makepkg --syncdeps --rmdeps --install --clean
```

Or

```sh
yay -S tube
```

#### Configuration

You now have to [configure the database](https://github.com/Chocobozzz/Tube/blob/develop/support/doc/production.md#database)
and credentials to it in the configuration file of Tube
in `/usr/share/webapps/tube/config/production.yaml`.

<div class="install-only-rc install-only-nightly" markdown="1">
Currently, there are no Arch packages available for RC or nightly builds of Tube. Please use the tarball:
{% include_relative installations/tarball.md %}
</div>


## CentOS

On CentOS, Fedora and RHEL, you can install Tube via a __community package__ made by [daftaupe](https://copr.fedorainfracloud.org/coprs/daftaupe/tube/) on COPR.

```sh
dnf copr enable daftaupe/tube
```

#### Prerequisites

You will need PostgreSQL, Node.JS and FFMpeg :

* Fedora you need RPM Fusion repository enabled
  ```sh
  sudo dnf install https://download1.rpmfusion.org/free/fedora/rpmfusion-free-release-$(rpm -E %fedora).noarch.rpm https://download1.rpmfusion.org/nonfree/fedora/rpmfusion-nonfree-release-$(rpm -E %fedora).noarch.rpm
  ```
* CentOS you will need EPEL and the unofficial EPEL-multimedia repositories enabled
  ```sh
  cd /etc/yum.repos.d && curl -O https://negativo17.org/repos/epel-multimedia.repo yum install https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm https://negativo17.org/repos/epel-multimedia.repo
  ```

**Setup the database**

```sh
su - postgres
initdb
createuser tube -W
createdb -O tube peertube_prod
echo "host peertube_prod tube 127.0.0.1/32 md5" >> data/pg_hba.conf
```

```sh
systemctl reload postgresql
```

**Start the services**

```sh
systemctl start redis
```

**Edit the configuration to fit your needs**

```sh
vim /etc/tube/production.yaml
```

**Start Tube and get the initial root / password**

```sh
systemctl start tube && journalctl -f -u tube
```

## YunoHost

On Debian running YunoHost, you can install Yarn, Node and Tube in one shot via a __community package__.

[![Install Peertube with YunoHost](https://install-app.yunohost.org/install-with-yunohost.png)](https://install-app.yunohost.org/?app=tube)

See [here](https://github.com/YunoHost-Apps/peertube_ynh) for support.

## Ansible on Debian

This is an alternate way of installing Peertube on Debian, provided by a community member.

The ansible playbook triggers the full installation of Peertube **without certbot** in a **reverse proxy** scenario, meaning HTTPS is not directly handled on the Peertube server.

This is for **Debian only**. All you need is to set the hostname, full qualified domain name, and wait about 15 minutes for the installation to go through.

 * **Repository:** [https://framagit.org/ansibleislife/ansible-role-tube](https://framagit.org/ansibleislife/ansible-role-tube/tree/master)
 * **HOWTO:** [https://framagit.org/ansibleislife/ansible-role-tube/blob/master/HOWTO.md](https://framagit.org/ansibleislife/ansible-role-tube/blob/master/HOWTO.md)


## Ansible and Docker Compose

This is a project that lets you automatically install Peertube with Ansible, using docker-compose.

The project is maintained by [@nutomic](https://github.com/Nutomic) and used on [tube.social](https://tube.social).

Features:
- automatic Let's Encrypt certificate handling via Traefik
- file caching with nginx (to limit backend access and Peertube CPU usage)
- email sending works out of the box

Project Link: https://yerbamate.ml/nutomic/tube-compose

## Kubernetes

You can deploy tube on [Kubernetes](https://kubernetes.io/) using the
[official docker image](https://tube.docs.dingshunyu.top/install-docker).

Two main options are available :

* Using [Helm](https://helm.sh/) with
  [tube-helm](https://git.lecygnenoir.info/LecygneNoir/tube-helm) which is
  initially forked from [jsl-helmcharts](https://gitlab.com/jsl3/helmcharts/-/tree/master/tube)

* Using Kubernetes deployments which supports storing content on [S3 remote storage](admin-remote-storage.md):
  [tube-on-kubernetes](https://forge.extranet.logilab.fr/open-source/tube-on-kubernetes)

* Using Kubernetes and [Kustomize](https://kustomize.io/) with Native Object storage 
  [tube-k8s](https://github.com/coopgo/tube-k8s)


## Shell and Docker Compose

A quick way to install the [official Docker stack](https://tube.docs.dingshunyu.top/install-docker) of tube **by one shell command** on a **bare server** with only Docker installed.

Features:

- install or upgrade of [Docker Compose](https://docs.docker.com/compose)
- build stack tree and official config files in `/var/tube`
- automatic fill of variables in `.env` with official template `MY_EMAIL_ADDRESS` and `MY_DOMAIN`
- generate PostgreSQL crendentials
- generate first Let's Encrypt certificate with [Certbot](https://certbot.eff.org/) container
- create systemd service
- run the stack

Also exposes the [CLI Server Tools](https://tube.docs.dingshunyu.top/maintain-tools?id=server-tools) and more for maintenance like:
- PostgreSQL tasks: dump, restore, psql requests
- nginx control
- stack upgrade with very-small downtime
- etc.

Project Link: https://github.com/kimsible/getpeer.tube

!> **Warning** Most these projects are in beta, so feedback is welcome to improve this type of deployment.

