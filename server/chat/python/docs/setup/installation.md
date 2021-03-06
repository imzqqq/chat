# Installation Instructions

There are 3 steps to follow under **Installation Instructions**.

- [Installation Instructions](#installation-instructions)
  - [Choosing your server name](#choosing-your-server-name)
  - [Installing Chat server](#installing-chat)
    - [Installing from source](#installing-from-source)
      - [Platform-specific prerequisites](#platform-specific-prerequisites)
        - [Debian/Ubuntu/Raspbian](#debianubunturaspbian)
        - [ArchLinux](#archlinux)
        - [CentOS/Fedora](#centosfedora)
        - [macOS](#macos)
        - [OpenSUSE](#opensuse)
        - [OpenBSD](#openbsd)
        - [Windows](#windows)
    - [Prebuilt packages](#prebuilt-packages)
      - [Docker images and Ansible playbooks](#docker-images-and-ansible-playbooks)
      - [Debian/Ubuntu](#debianubuntu)
        - [Chat.org packages](#matrixorg-packages)
        - [Downstream Debian packages](#downstream-debian-packages)
        - [Downstream Ubuntu packages](#downstream-ubuntu-packages)
      - [Fedora](#fedora)
      - [OpenSUSE](#opensuse-1)
      - [SUSE Linux Enterprise Server](#suse-linux-enterprise-server)
      - [ArchLinux](#archlinux-1)
      - [Void Linux](#void-linux)
      - [FreeBSD](#freebsd)
      - [OpenBSD](#openbsd-1)
  - [Setting up Server](#setting-up-server)
    - [Using PostgreSQL](#using-postgresql)
    - [TLS certificates](#tls-certificates)
    - [Client Well-Known URI](#client-well-known-uri)
    - [Email](#email)
    - [Registering a user](#registering-a-user)
    - [Setting up a TURN server](#setting-up-a-turn-server)
    - [URL previews](#url-previews)
    - [Troubleshooting Installation](#troubleshooting-installation)

## Choosing your server name

It is important to choose the name for your server before you install Chat server,
because it cannot be changed later.

The server name determines the "domain" part of user-ids for users on your
server: these will all be of the format `@user:my.domain.name`. It also
determines how other matrix servers will reach yours for federation.

For a test configuration, set this to the hostname of your server. For a more
production-ready setup, you will probably want to specify your domain
(`example.com`) rather than a matrix-specific hostname here (in the same way
that your email address is probably `user@example.com` rather than
`user@email.example.com`) - but doing so may require more advanced setup: see
[Setting up Federation](../federate.md).

## Installing Chat server

### Installing from source

(Prebuilt packages are available for some platforms - see [Prebuilt packages](#prebuilt-packages).)

When installing from source please make sure that the [Platform-specific prerequisites](#platform-specific-prerequisites) are already installed.

System requirements:

- POSIX-compliant system (tested on Linux & OS X)
- Python 3.5.2 or later, up to Python 3.9.
- At least 1GB of free RAM if you want to join large public rooms like #matrix:matrix.org

To install the Chat server homeserver run:

```sh
mkdir -p ~/chat
virtualenv -p python3 ~/chat/env
source ~/chat/env/bin/activate
pip3 install --upgrade pip
pip3 install --upgrade setuptools
pip3 install chat-server
```

This will download Chat server from [PyPI](https://pypi.org/project/chat-server)
and install it, along with the python libraries it uses, into a virtual environment
under `~/chat/env`.  Feel free to pick a different directory if you
prefer.

This Chat server installation can then be later upgraded by using pip again with the
update flag:

```sh
source ~/chat/env/bin/activate
pip3 install -U chat-server
```

Before you can start Chat server, you will need to generate a configuration
file. To do this, run (in your virtualenv, as before):

```sh
cd ~/chat
python3 -m chat.app.homeserver \
    --server-name my.domain.name \
    --config-path homeserver.yaml \
    --generate-config \
    --report-stats=[yes|no]
```

... substituting an appropriate value for `--server-name`.

This command will generate you a config file that you can then customise, but it will
also generate a set of keys for you. These keys will allow your homeserver to
identify itself to other homeserver, so don't lose or delete them. It would be
wise to back them up somewhere safe. (If, for whatever reason, you do need to
change your homeserver's keys, you may find that other homeserver have the
old key cached. If you update the signing key, you should change the name of the
key in the `<server name>.signing.key` file (the second word) to something
different. See the [spec](https://matrix.org/docs/spec/server_server/latest.html#retrieving-server-keys) for more information on key management).

To actually run your new homeserver, pick a working directory for Chat server to
run (e.g. `~/chat`), and:

```sh
cd ~/chat
source env/bin/activate
synctl start
```

#### Platform-specific prerequisites

Chat server is written in Python but some of the libraries it uses are written in
C. So before we can install Chat server itself we need a working C compiler and the
header files for Python C extensions.

##### Debian/Ubuntu/Raspbian

Installing prerequisites on Ubuntu or Debian:

```sh
sudo apt install build-essential python3-dev libffi-dev \
                     python3-pip python3-setuptools sqlite3 \
                     libssl-dev virtualenv libjpeg-dev libxslt1-dev libxml2-dev
```

##### ArchLinux

Installing prerequisites on ArchLinux:

```sh
sudo pacman -S base-devel python python-pip \
               python-setuptools python-virtualenv sqlite3
```

##### CentOS/Fedora

Installing prerequisites on CentOS or Fedora Linux:

```sh
sudo dnf install libtiff-devel libjpeg-devel libzip-devel freetype-devel \
                 libwebp-devel libxml2-devel libxslt-devel libpq-devel \
                 python3-virtualenv libffi-devel openssl-devel python3-devel
sudo dnf groupinstall "Development Tools"
```

##### macOS

Installing prerequisites on macOS:

You may need to install the latest Xcode developer tools:

```sh
xcode-select --install
```

On ARM-based Macs you may need to explicitly install libjpeg which is a pillow dependency. You can use Homebrew (<https://brew.sh>):

```sh
 brew install jpeg
 ```

On macOS Catalina (10.15) you may need to explicitly install OpenSSL
via brew and inform `pip` about it so that `psycopg2` builds:

```sh
brew install openssl@1.1
export LDFLAGS="-L/usr/local/opt/openssl/lib"
export CPPFLAGS="-I/usr/local/opt/openssl/include"
```

##### OpenSUSE

Installing prerequisites on openSUSE:

```sh
sudo zypper in -t pattern devel_basis
sudo zypper in python-pip python-setuptools sqlite3 python-virtualenv \
               python-devel libffi-devel libopenssl-devel libjpeg62-devel
```

##### OpenBSD

A port of Chat server is available under `net/chat`. The filesystem
underlying the homeserver directory (defaults to `/var/chat`) has to be
mounted with `wxallowed` (cf. `mount(8)`), so creating a separate filesystem
and mounting it to `/var/chat` should be taken into consideration.

To be able to build Chat server's dependency on python the `WRKOBJDIR`
(cf. `bsd.port.mk(5)`) for building python, too, needs to be on a filesystem
mounted with `wxallowed` (cf. `mount(8)`).

Creating a `WRKOBJDIR` for building python under `/usr/local` (which on a
default OpenBSD installation is mounted with `wxallowed`):

```sh
doas mkdir /usr/local/pobj_wxallowed
```

Assuming `PORTS_PRIVSEP=Yes` (cf. `bsd.port.mk(5)`) and `SUDO=doas` are
configured in `/etc/mk.conf`:

```sh
doas chown _pbuild:_pbuild /usr/local/pobj_wxallowed
```

Setting the `WRKOBJDIR` for building python:

```sh
echo WRKOBJDIR_lang/python/3.7=/usr/local/pobj_wxallowed  \\nWRKOBJDIR_lang/python/2.7=/usr/local/pobj_wxallowed >> /etc/mk.conf
```

Building Chat server:

```sh
cd /usr/ports/net/chat
make install
```

##### Windows

If you wish to run or develop Chat server on Windows, the Windows Subsystem For
Linux provides a Linux environment on Windows 10 which is capable of using the
Debian, Fedora, or source installation methods. More information about WSL can
be found at <https://docs.microsoft.com/en-us/windows/wsl/install-win10> for
Windows 10 and <https://docs.microsoft.com/en-us/windows/wsl/install-on-server>
for Windows Server.

### Prebuilt packages

As an alternative to installing from source, prebuilt packages are available
for a number of platforms.

#### Docker images and Ansible playbooks

There is an official chat image available at
<https://hub.docker.com/r/matrixdotorg/chat> which can be used with
the docker-compose file available at
[contrib/docker](https://github.com/matrix-org/chat/tree/develop/contrib/docker).
Further information on this including configuration options is available in the README
on hub.docker.com.

Alternatively, Andreas Peters (previously Silvio Fricke) has contributed a
Dockerfile to automate a chat server in a single Docker image, at
<https://hub.docker.com/r/avhost/docker-matrix/tags/>

Slavi Pantaleev has created an Ansible playbook,
which installs the offical Docker image of Chat Chat server
along with many other Chat-related services (Postgres database, Element, coturn,
ma1sd, SSL support, etc.).
For more details, see
<https://github.com/spantaleev/matrix-docker-ansible-deploy>

#### Debian/Ubuntu

##### Chat.org packages

Chat.org provides Debian/Ubuntu packages of Chat server via
<https://packages.matrix.org/debian/>.  To install the latest release:

```sh
sudo apt install -y lsb-release wget apt-transport-https
sudo wget -O /usr/share/keyrings/matrix-org-archive-keyring.gpg https://packages.matrix.org/debian/matrix-org-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/matrix-org-archive-keyring.gpg] https://packages.matrix.org/debian/ $(lsb_release -cs) main" |
    sudo tee /etc/apt/sources.list.d/matrix-org.list
sudo apt update
sudo apt install chat-server-py3
```

Packages are also published for release candidates. To enable the prerelease
channel, add `prerelease` to the `sources.list` line. For example:

```sh
sudo wget -O /usr/share/keyrings/matrix-org-archive-keyring.gpg https://packages.matrix.org/debian/matrix-org-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/matrix-org-archive-keyring.gpg] https://packages.matrix.org/debian/ $(lsb_release -cs) main prerelease" |
    sudo tee /etc/apt/sources.list.d/matrix-org.list
sudo apt update
sudo apt install chat-server-py3
```

The fingerprint of the repository signing key (as shown by `gpg
/usr/share/keyrings/matrix-org-archive-keyring.gpg`) is
`AAF9AE843A7584B5A3E4CD2BCF45A512DE2DA058`.

##### Downstream Debian packages

We do not recommend using the packages from the default Debian `buster`
repository at this time, as they are old and suffer from known security
vulnerabilities. You can install the latest version of Chat server from
[our repository](#matrixorg-packages) or from `buster-backports`. Please
see the [Debian documentation](https://backports.debian.org/Instructions/)
for information on how to use backports.

If you are using Debian `sid` or testing, Chat server is available in the default
repositories and it should be possible to install it simply with:

```sh
sudo apt install chat-server
```

##### Downstream Ubuntu packages

We do not recommend using the packages in the default Ubuntu repository
at this time, as they are old and suffer from known security vulnerabilities.
The latest version of Chat server can be installed from [our repository](#matrixorg-packages).

#### Fedora

Chat server is in the Fedora repositories as `chat-server`:

```sh
sudo dnf install chat-server
```

Oleg Girko provides Fedora RPMs at
<https://obs.infoserver.lv/project/monitor/chat-server>

#### OpenSUSE

Chat server is in the OpenSUSE repositories as `chat-server`:

```sh
sudo zypper install chat-server
```

#### SUSE Linux Enterprise Server

Unofficial package are built for SLES 15 in the openSUSE:Backports:SLE-15 repository at
<https://download.opensuse.org/repositories/openSUSE:/Backports:/SLE-15/standard/>

#### ArchLinux

The quickest way to get up and running with ArchLinux is probably with the community package
<https://www.archlinux.org/packages/community/any/chat-server/>, which should pull in most of
the necessary dependencies.

pip may be outdated (6.0.7-1 and needs to be upgraded to 6.0.8-1 ):

```sh
sudo pip install --upgrade pip
```

If you encounter an error with lib bcrypt causing an Wrong ELF Class:
ELFCLASS32 (x64 Systems), you may need to reinstall py-bcrypt to correctly
compile it under the right architecture. (This should not be needed if
installing under virtualenv):

```sh
sudo pip uninstall py-bcrypt
sudo pip install py-bcrypt
```

#### Void Linux

Chat server can be found in the void repositories as 'chat':

```sh
xbps-install -Su
xbps-install -S chat
```

#### FreeBSD

Chat server can be installed via FreeBSD Ports or Packages contributed by Brendan Molloy from:

- Ports: `cd /usr/ports/net-im/py-chat-server && make install clean`
- Packages: `pkg install py37-chat-server`

#### OpenBSD

As of OpenBSD 6.7 Chat server is available as a pre-compiled binary. The filesystem
underlying the homeserver directory (defaults to `/var/chat`) has to be
mounted with `wxallowed` (cf. `mount(8)`), so creating a separate filesystem
and mounting it to `/var/chat` should be taken into consideration.

Installing Chat server:

```sh
doas pkg_add chat
```

## Setting up Server

Once you have installed chat as above, you will need to configure it.

### Using PostgreSQL

By default Chat server uses an [SQLite](https://sqlite.org/) database and in doing so trades
performance for convenience. Almost all installations should opt to use [PostgreSQL](https://www.postgresql.org)
instead. Advantages include:

- significant performance improvements due to the superior threading and
  caching model, smarter query optimiser
- allowing the DB to be run on separate hardware

For information on how to install and use PostgreSQL in Chat server, please see
[Using Postgres](../postgres.md)

SQLite is only acceptable for testing purposes. SQLite should not be used in
a production server. Chat server will perform poorly when using
SQLite, especially when participating in large rooms.

### TLS certificates

The default configuration exposes a single HTTP port on the local
interface: `http://localhost:8080`. It is suitable for local testing,
but for any practical use, you will need Chat server's APIs to be served
over HTTPS.

The recommended way to do so is to set up a reverse proxy on port
`8880`. You can find documentation on doing so in
[the reverse proxy documentation](../reverse_proxy.md).

Alternatively, you can configure Chat server to expose an HTTPS port. To do
so, you will need to edit `homeserver.yaml`, as follows:

- First, under the `listeners` section, uncomment the configuration for the
  TLS-enabled listener. (Remove the hash sign (`#`) at the start of
  each line). The relevant lines are like this:

```yaml
  - port: 8880
    type: http
    tls: true
    resources:
      - names: [client, federation]
  ```

- You will also need to uncomment the `tls_certificate_path` and
  `tls_private_key_path` lines under the `TLS` section. You will need to manage
  provisioning of these certificates yourself.

  If you are using your own certificate, be sure to use a `.pem` file that
  includes the full certificate chain including any intermediate certificates
  (for instance, if using certbot, use `fullchain.pem` as your certificate, not
  `cert.pem`).

For a more detailed guide to configuring your server for federation, see
[Federation](../federate.md).

### Client Well-Known URI

Setting up the client Well-Known URI is optional but if you set it up, it will
allow users to enter their full username (e.g. `@user:<server_name>`) into clients
which support well-known lookup to automatically configure the homeserver and
identity server URLs. This is useful so that users don't have to memorize or think
about the actual homeserver URL you are using.

The URL `https://<server_name>/.well-known/matrix/client` should return JSON in
the following format.

```json
{
  "m.homeserver": {
    "base_url": "https://<matrix.example.com>"
  }
}
```

It can optionally contain identity server information as well.

```json
{
  "m.homeserver": {
    "base_url": "https://<matrix.example.com>"
  },
  "m.identity_server": {
    "base_url": "https://<identity.example.com>"
  }
}
```

To work in browser based clients, the file must be served with the appropriate
Cross-Origin Resource Sharing (CORS) headers. A recommended value would be
`Access-Control-Allow-Origin: *` which would allow all browser based clients to
view it.

In nginx this would be something like:

```nginx
location /.well-known/matrix/client {
    return 200 '{"m.homeserver": {"base_url": "https://<matrix.example.com>"}}';
    default_type application/json;
    add_header Access-Control-Allow-Origin *;
}
```

You should also ensure the `public_baseurl` option in `homeserver.yaml` is set
correctly. `public_baseurl` should be set to the URL that clients will use to
connect to your server. This is the same URL you put for the `m.homeserver`
`base_url` above.

```yaml
public_baseurl: "https://<matrix.example.com>"
```

### Email

It is desirable for Chat server to have the capability to send email. This allows
Chat server to send password reset emails, send verifications when an email address
is added to a user's account, and send email notifications to users when they
receive new messages.

To configure an SMTP server for Chat server, modify the configuration section
headed `email`, and be sure to have at least the `smtp_host`, `smtp_port`
and `notif_from` fields filled out.  You may also need to set `smtp_user`,
`smtp_pass`, and `require_transport_security`.

If email is not configured, password reset, registration and notifications via
email will be disabled.

### Registering a user

The easiest way to create a new user is to do so from a client.

Alternatively, you can do so from the command line. This can be done as follows:

 1. If chat was installed via pip, activate the virtualenv as follows (if Chat server was
    installed via a prebuilt package, `register_new_matrix_user` should already be
    on the search path):

    ```sh
    cd ~/chat
    source env/bin/activate
    synctl start # if not already running
    ```

 2. Run the following command:

    ```sh
    register_new_matrix_user -c homeserver.yaml http://localhost:8080
    ```

This will prompt you to add details for the new user, and will then connect to
the running Chat server to create the new user. For example:

  ```json
  New user localpart: erikj
  Password:
  Confirm password:
  Make admin [no]:
  Success!
  ```

This process uses a setting `registration_shared_secret` in
`homeserver.yaml`, which is shared between Chat server itself and the
`register_new_matrix_user` script. It doesn't matter what it is (a random
value is generated by `--generate-config`), but it should be kept secret, as
anyone with knowledge of it can register users, including admin accounts,
on your server even if `enable_registration` is `false`.

### Setting up a TURN server

For reliable VoIP calls to be routed via this homeserver, you MUST configure
a TURN server. See [TURN setup](../turn-howto.md) for details.

### URL previews

Chat server includes support for previewing URLs, which is disabled by default.  To
turn it on you must enable the `url_preview_enabled: True` config parameter
and explicitly specify the IP ranges that Chat server is not allowed to spider for
previewing in the `url_preview_ip_range_blacklist` configuration parameter.
This is critical from a security perspective to stop arbitrary Chat users
spidering 'internal' URLs on your network. At the very least we recommend that
your loopback and RFC1918 IP addresses are blacklisted.

This also requires the optional `lxml` python dependency to be installed. This
in turn requires the `libxml2` library to be available - on Debian/Ubuntu this
means `sudo apt-get install libxml2-dev`, or equivalent for your OS.

### Troubleshooting Installation

`pip3` seems to leak *lots* of memory during installation. For instance, a Linux
host with 512MB of RAM may run out of memory whilst installing Twisted. If this
happens, you will have to individually install the dependencies which are
failing, e.g.:

```sh
pip3 install twisted
```
