<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<div align="center">
  <h1>🐱‍👤Chat</h1>
  <p><strong>Chat</strong> is a decentralized interoperable data transmission ecosystem.</p>
</div>

## Install prerequisites

### APT

    sudo apt install build-essential python-dev python3-dev libffi-dev \
                     python3-pip python3-setuptools sqlite3 libssl-dev \
                     virtualenv libjpeg-dev libxslt1-dev libpq-dev libpq5

### npm | cnpm | yarn | tyarn

- Install node `sudo apt install nodejs`,  Once installed, verify it by checking the installed version using the following command: `node -v`
- Install cnpm `npm --global install cnpm`
- Install yarn `npm --global install yarn`
- Install tyarn `npm --global install tyarn`
- Managing multiple versions of Node.js using n: `npm install -g n`, `n ls-remote`, `n install 10.22.0`, `n use 10.22.0`
- npm install -g typescript

### Rust | mdbook

- Install rust `sudo apt install rustc`
- Set up PATH `vi ~/.bash_profile`, `export PATH=/root/.cargo/bin`
- Run `cargo install mdbook`

### Hugo

- Ubuntu:
  1. Use apt: `sudo apt-get install hugo` (Currently not working)
  2. Use snap:

```bash
sudo apt update
sudo apt install snapd
# In any of the Linux distributions that support snaps, you may install the “extended” Sass/SCSS version with this command
snap install hugo --channel=extended
```

  3. Use dpkg:

```bash
wget https://github.com/gohugoio/hugo/releases/download/v0.88.1/hugo_extended_0.88.1_Linux-64bit.deb
sudo dpkg -i hugo_extended_0.88.1_Linux-64bit.deb

# Build and Install the Binaries from Source Instead
mkdir $HOME/src
cd $HOME/src
git clone https://github.com/gohugoio/hugo.git
cd hugo
CGO_ENABLED=1 go install --tags extended
```

- Mac: `brew install hugo`
- Docker: `docker pull klakegg/hugo`, then run:

```bash
  # Normal build
  docker run --rm -it \
   -v $(pwd):/src \
   klakegg/hugo:latest

  # Run server
  docker run --rm -it \
   -v $(pwd):/src \
   -p 1314:1314 \
   klakegg/hugo:latest\
   server
```

### mdbook

Run command: `cargo install --git https://github.com/rust-lang/mdBook.git mdbook`

### Jekyll

- Install Ruby and other prerequisites:

```sh
sudo apt-get install ruby-full build-essential zlib1g-dev
```

- Avoid installing RubyGems packages (called gems) as the root user. Instead, set up a gem installation directory for your user account. The following commands will add environment variables to your ~/.bashrc file to configure the gem installation path:

```sh
echo '# Install Ruby Gems to ~/gems' >> ~/.bashrc
echo 'export GEM_HOME="$HOME/gems"' >> ~/.bashrc
echo 'export PATH="$HOME/gems/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

- Install Jekyll and Bundler:

```sh
gem install jekyll bundler
```

- Build the site and make it available on a local server.

```sh
bundle exec jekyll serve
```

- Browse to <http://localhost:4000>

### Python3 | Pip3

> We may need to use `python -m pip`

- Update and Refresh Repository Lists: `sudo apt update`
- Install Supporting Software: `sudo apt install software-properties-common`
- Add Deadsnakes PPA: `sudo add-apt-repository ppa:deadsnakes/ppa`
- Install Python 3: `sudo apt install python3`
- Allow the process to complete and verify the Python version was installed successfully: `python3 --version`
- Install the python3-venv virtual environment: `sudo apt install python3-venv`, or `apt-get install python3.9-dev python3.9-venv`
- Install local deps: `pip3 install -e <path>`
- User level -> Open current user's `~/.bashrc` file `vi ~/.bashrc` and add new alias：`alias python='/usr/bin/python3.9'`
- Specify pip with python: `python -m pip --version`
- `pip3 install --upgrade setuptools`, `pip3 install --upgrade pip`, `pip3 install --upgrade distlib`
- `pip3 show package_name`
- `python -m pip install psycopg2-binary`
- Uninstall python-psycopg2 and its dependencies: `sudo apt-get remove python-psycopg2`, if python throws `pip cannot uninstall <package>: "It is a distutils installed project"` error, then run: `pip install --ignore-installed [package name]==[package version]`

### PostgreSQL

- Install Postgres:

```bash
# Create the file repository configuration:
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'

# Import the repository signing key:
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -

# Update the package lists:
sudo apt-get update

# Install the latest version of PostgreSQL.
# If you want a specific version, use 'postgresql-12' or similar instead of 'postgresql':
sudo apt-get -y install postgresql
```

- Connect to PostgreSQL(pwd: newpwd): `sudo su - postgres`
- Start PostgreSQL server: `sudo service postgresql start`
- Now open a postgress prompt using the command: `psql`
- Create a new user for the database: `CREATE USER imzqqq WITH PASSWORD '*****';`
- Upgrade imzqqq to become super user: `ALTER USER imzqqq WITH SUPERUSER;`
- Create a user database specifying `Collate` and `Ctype`: `CREATE DATABASE chat WITH ENCODING='UTF8' LC_COLLATE='C' LC_CTYPE='C' TEMPLATE=template0 OWNER=imzqqq;`
- (Optional) Delete database: `DROP DATABASE chat`
- Grant all permissions on the chat database to imzqqq: `GRANT ALL PRIVILEGES ON DATABASE chat TO imzqqq;`
- Quit: `\q`
- Create a common Linux user with the same name as the new database user we just created: `sudo adduser imzqqq`, `sudo passwd imzqqq`
- Connect to the database chat as imzqqq: `su - imzqqq`, `psql -d chat`
- postgresql force delete database`SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE datname='db_name' AND pid<>pg_backend_pid();` then run `drop database db_name;`

## Set the user as administrator

- Mark a user as server admin: `UPDATE users SET admin = 1 WHERE name = '@notices:chat.dingshunyu.top';`

## gitlab

```bash
export GITLAB_HOME=/srv/gitlab

sudo docker run --detach  --hostname gitlab.dingshunyu.top --publish 16443:443 --publish 16680:80 --publish 16622:22  --name gitlab --restart always --volume $GITLAB_HOME/config:/etc/gitlab  --volume $GITLAB_HOME/logs:/var/log/gitlab  --volume $GITLAB_HOME/data:/var/opt/gitlab  gitlab/gitlab-ee:latest

# sudo docker run --detach  --hostname gitlab.dingshunyu.top --publish 16443:443 --publish 16680:80 --publish 16622:22  --name gitlab --restart always --volume $GITLAB_HOME/config:/etc/gitlab:Z  --volume $GITLAB_HOME/logs:/var/log/gitlab:Z  --volume $GITLAB_HOME/data:/var/opt/gitlab:Z  gitlab/gitlab-ee:latest

sudo docker exec -it gitlab /bin/bash
sudo docker exec -it gitlab editor /etc/gitlab/gitlab.rb

docker stop/restart
sudo docker logs -f gitlab
sudo docker exec -it gitlab grep 'Password:' /etc/gitlab/initial_root_password
sudo gitlab-rake "gitlab:password:reset"
sudo gitlab-rake "gitlab:password:reset[johndoe]"
```

## Locale

Run the `locale` command to view the character encoding type of the ECS instance and switch the character encoding type from non-en_US.UTF-8 to en_US.UTF-8. Run the following command to switch the character encoding type.

`LANG=en_US.UTF-8`

If the problem is still not solved, run the following command to switch the character encoding type.

`export LC_ALL=en_US.UTF-8`

If the problem is still not solved, run the following command to switch the character encoding type.

`localectl set-locale LANG=en_US.UTF-8`

If the above solution does not solve the problem, run the following command to switch the character encoding type.

`export LANGUAGE=en_US.UTF-8`

If the problem is still not resolved, you should try running the following command to restart the ECS instance.

`reboot`

## tar

TAR command details
These five are separate commands, one of which is required for compression and decompression, and can be used with other commands but only one of them.

-c: Create compressed file
-x: Decompress
-t: View content
-r: Append files to the end of the compressed archive
-u: Update the files in the original package

The following parameters are optional when compressing or decompressing files as needed。

-z: With gzip attribute
-j: With bz2 attribute
-Z: With compress attribute
-v: Show all processes
-O: Unpack files to standard output

- Compression

```sh
tar –cvf jpg.tar *.jpg
tar –czf jpg.tar.gz *.jpg
tar –cjf jpg.tar.bz2 *.jpg
tar –cZf jpg.tar.Z *.jpg
```

- Decompression

```sh
tar –xvf file.tar
tar -xzvf file.tar.gz
tar -xjvf file.tar.bz2
```


## Architecture

- server
  - flow
    - [go](./server/flow/go/README.md)
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[ruby](./server/flow/ruby/README.md)
      - <i class="fas fa-check" style="color:green; margin-right:5px;"></i>[docs](./server/flow/ruby/docs/README.md), <http://localhost:1313>
      - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[omniauth-mastodon](./server/flow/ruby/omniauth-mastodon/README.md)
	- rust
	- python
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[join-flow](./server/flow/ruby/joinmastodon/README.md), <http://localhost:3002>
  - chat
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[go](./server/chat/go/README.md),
      - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[dugong](./server/chat/go/dugong/README.md),
      - <i class="fa fa-running" style="color:#ff922b; margin-right:5px;"></i>[go-http-js-libp2p](./server/chat/go/go-http-js-libp2p/README.md),
      - <i class="fa fa-running" style="color:#ff922b; margin-right:5px;"></i>[go-sqllite3-js](./server/chat/go/go-sqlite3-js/README.md),
      - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[gomatrix](./server/chat/go/gomatrix/README.md),,
      - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[gomatrixserverlib](./server/chat/go/gomatrixserverlib/README.md),
      - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[naffka](./server/chat/go/naffka/README.md),
      - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[pinecone](./server/chat/go/pinecone/README.md),
      - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[util](./server/chat/go/util/README.md),
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[python](./server/chat/python/README.rst), <http://localhost:8080>, <http://localhost:8081>, <http://localhost:8082>
      - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[matrix-synapse-ldap3](./server/chat/python/matrix-synapse-ldap3/README.rst),
	  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[api-spec](./server/chat/python/api-specification/README.md), <http://localhost:1314>
	  - <i class="fa fa-running" style="color:#ff922b; margin-right:5px;"></i>bridges
		- <i class="fa fa-running" style="color:#ff922b; margin-right:5px;"></i>[wechat-appservices](./server/chat/bridges/wechat-appservice/README.md)
      - <i class="fas fa-check" style="color:green; margin-right:5px;"></i>[canonicaljson](./server/chat/python/canonicaljson/README.md), <http://localhost:3004>
	  - <i class="fa fa-running" style="color:#ff922b; margin-right:5px;"></i>[chat-bot](./server/chat/python/chat-bot/README.md)
		- <i class="fa fa-running" style="color:#ff922b; margin-right:5px;"></i>[matrix-nio](./server/chat/python/chat-bot/matrix-nio/README.md)
      - <i class="fas fa-check" style="color:green; margin-right:5px;"></i>[docs](./server/chat/python/docs/README.md),
	- rust
	- cc
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[server-admin](./server/server-admin/README.md), <http://localhost:3001>
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[tube](./server/tube/README.md), <https://localhost:8083>
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[to](./to/README.md), <http://localhost:5000>
  - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[pantalaimon](./pantalaimon/README.md)
  - <i class="fas fa-check" style="color:green; margin-right:5px;"></i>[sydent](./sydent/README.rst), <http://localhost:8090>
  - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[sygnal](./sygnal/README.rst),
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[user-verification-service](./user-verification-service/README.md), <http://localhost:3003>
  - <i class="fa fa-running" style="color:#ff922b; margin-right:5px;"></i>[mjolnir](./mjolnir/README.md),
  - <i class="fa fa-check" style="color:#ff922b; margin-right:5px;"></i>[content-scanner](./content-scanner/README.md), <http://localhost:9000>
  - bridges
    - <i class="fa fa-running" style="color:green; margin-right:5px;"></i>[matrix-bot-sdk](./matrix-bot-sdk/README.md), 
  - flow-apns
  - tube
  - bug-report


- deploy


- libs
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[olm](./olm/README.md), 


- shared
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[ios](./shared/ios/README.md), 
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[flow](./shared/ios/Riot/Flow/README.md),
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[android](./shared/android/README.md),
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[flow](./shared/android/README.md/#Flow),
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[desktop](./shared/desktop/README.md), <http://localhost:8084>
    - <i class="fas fa-check" style="color:green; margin-right:5px;"></i>[eslint-plugin-matrix-org](./shared/desktop/eslint-plugin-matrix-org/README.md),
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[matrix-mock-request](./shared/desktop/matrix-mock-request/README.md),
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[matrix-react-test-utils](./shared/desktop/matrix-react-test-utils/README.md),
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[matrix-web-i18n](./shared/desktop/matrix-web-i18n/README.md),
    - <i class="fa fa-check" style="color:#ff922b; margin-right:5px;"></i>[matrix-widget-api](./shared/desktop/matrix-widget-api/README.md), Not Ready
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[seshat](./shared/desktop/seshat/README.md),
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[matrix-react-sdk](./shared/desktop/seshat/README.md),
    - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[matrix-js-sdk](./shared/desktop/seshat/README.md),


- test
  - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[complement](./complement/README.md)
  - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[sytest](./sytest/README.rst),
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[federation-tester](./federation-tester/README.md), <http://localhost:8085/api/report?server_name=localhost:8082>
  - <i class="fas fa-running" style="color:#ff922b; margin-right:5px;"></i>[matrix-is-tester](./matrix-is-tester/README.md), 


- website
  - <i class="fa fa-check" style="color:green; margin-right:5px;"></i>[official-site](./websites/official-site/README.md), <http://localhost:8000>
  - <i class="fa fa-running" style="color:green; margin-right:5px;"></i>[apps](./websites/official-site2/README.md)
  - live
  - space

## API Tests

- <https://chat.imzqqq.top/chat/client/r0/login>
- <https://chat.imzqqq.top/chat/static/#/>
- <http://localhost:8080/chat/static/#/>
- <http://localhost:8080>
- <http://localhost:8081>
- <http://localhost:8082>

## Encoding

> Use LF in Linux, use CRLF in Windows (Optional)

```bash
git config --global core.autocrlf false
git config --global core.safecrlf true
```

## Code of conduct

```txt
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

### Type

type is used to specify the type of commit, and must be one of the following types.

- feat: new features, new functions
- fix: modify a bug
- perf: code changes to improve performance (optimizing the performance of the program without affecting the internal behavior of the code)
- refactor: code refactoring (refactoring, code changes that do not affect the internal behavior of the code, functionality)
- docs: documentation changes
- style: code formatting changes, not css changes (e.g. semicolon changes)
- test: test case addition, modification
- build: modifications that affect project builds or dependencies
- revert: restore the last commit
- ci: continuous integration related file changes
- chore: other changes (changes that are not in the above categories)
- release: release a new version
- workflow: workflow-related file changes

### Scope

scope is used to specify the scope of the commit, use * when there are multiple scopes.

### Subject

The subject is used as a concise description of the changes to the commit.

Use an imperative, usually starting with the original verb, e.g. change instead of changed or changes
first letter lowercase
without a period at the end (.)

### Body

The body is used to describe the commit in detail. Use imperative sentences, usually starting with the original verb form, e.g. use change instead of changed or changes.

The body should contain the motivation for the change and a comparison with the previous action.

### Footer

The footer is currently used in two cases.

1. Incompatible changes.
    All incompatible changes must be described in the footer area, starting with BREAKING CHANGE:, followed by a description of the change, the reason for the change, and a migration comment.

2. Closing an issue.
    If the commit is for an issue, you can close the issue in the footer.

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.11.1/css/all.css">

## Common sense

```sh
$ cp path-to-file/*.service /etc/systemd/system/
$ systemctl daemon-reload
$ systemctl enable --now service-name.service
$ journalctl -u service-name.service
$ git submodule update --init --recursive
$ git config http.postBuffer 524288000
$ git config --global --list
$ git config --global user.name "user_name"
$ git config --global user.email "email_address"
$ ssh-keygen -t rsa -C "email_address"
$ git config --global https.proxy http://127.0.0.1:1080
$ git config --global https.proxy https://127.0.0.1:1080
$ git config --global --unset http.proxy
$ git config --global --unset https.proxy
$ npm config delete proxy
$ sudo nvidia-docker run -p 11050:11250 --name=zq_pytorch -v ~/workspace:/root/workspace -v /data:/root/data --shm-size 64g --device /dev/nvidiactl --device /dev/nvidia-uvm --device /dev/nvidia0 --device /dev/nvidia1 --device /dev/nvidia2 --device /dev/nvidia3 -it hetao-pytorch /bin/bash

$ npm install uuid
$ npm install --global windows-build-tools
$ npm config set python c:\python27\python.exe

$ sudo nginx -s reload

$ sudo ufw enable
$ sudo ufw allow smtp　#25/tcp (smtp)
$ sudo ufw allow 22/tcp #22/tcp (ssh)
$ sudo ufw allow 53
$ sudo ufw allow from 192.168.1.100 
$ sudo ufw allow proto udp 192.168.0.1 port 53 to 192.168.0.2 port 53
$ sudo ufw deny smtp
$ sudo ufw delete allow smtp 
$ sudo ufw status

$ sudo apt install maven
$ sudo apt install openjdk-8-jdk

$ du-h --max-depth=1

$ git push origin --delete branch_name
$ git branch -a 
$ git branch -d branch_name

$ pip install virtualenvwrapper
$ mkvirtualenv project_env
$ mkvirtualenv env --python=python2.7
$ workon project_env
$ rmvirtualenv project_env
$ lssitepackages
# lsvirtualenv

$ sudo gem install -n /usr/local/bin cocoapods
$ sudo adduser username
$ sudo alter user admin superuser login
$ sudo journalctl -u mastodon-sidekiq
$ systemctl restart mastodon-sidekiq
$ systemctl reload mastodon-web
$ systemctl restart mastodon-streaming
$ systemctl list-unit-files|grep enabled
$ su - imzqqq
$ bundle config deployment &apos;true&apos;
$ bundle config without &apos;development test&apos;
$ bundle install -j$(getconf _NPROCESSORS_ONLN)
$ yarn install --pure-lockfile
$ RAILS_ENV=production bundle exec rake mastodon:setup
$ exit
$ cp /home/mastodon/live/dist/nginx.conf /etc/nginx/sites-available/mastodon
$ ln -s /etc/nginx/sites-available/mastodon /etc/nginx/sites-enabled/mastodon
$ certbot --nginx -d example.com
$ cp /home/mastodon/live/dist/mastodon-*.service /etc/systemd/system/
$ systemctl daemon-reload
$ systemctl enable --now mastodon-web mastodon-sidekiq mastodon-streaming

$ git remote set-url origin <remote_url>
$ git clone -b <remote_url>
$ pip3 install psycopg2-binary
$ sudo apt-get install -y nodejs
$ sudo apt-get install gcc g++ make
$ curl -sL https://dl.yarnpkg.com/debian/pubkey.gpg|gpg--dearmor| sudo tee /usr/share/keyrings/yarnkey.gpg >/dev/null
echo &quot;deb [signed-by=/usr/share/keyrings/yarnkey.gpg] https://dl.yarnpkg.com/debianstablemain&quot; | sudo tee /etc/apt/sources.list.d/yarn.list
$ sudo apt-get update sudo apt-get install yarn

$ git fetch
$ git checkout <branch_name>

$ git remote -v
$ git remote add upstream <upstream_server_url>
$ git remote -v
$ git fetch upstream
$ git checkout main
$ git merge upstream/main
$ git push origin main

$ git config --global core.autocrlf false
$ git config --global core.safecrlf true

$ go env -w GOPROXY=https://goproxy.cn,https://gocenter.io,https://goproxy.io,direct

$ git push origin local_branch:remote_branch
$ sudo chown -R user_name /path/to/folder

$ python3 -m pip install -U autopep8
$ npm install yarn tyarn -g

$ alias python=&apos;/usr/bin/python3&apos;
$ alias pip=&apos;/usr/bin/pip3&apos;
$ . ~/.bashrc

$ brew install postgresql
$ brew install pgadmin4
$ initdb /usr/local/var/postgres -E utf8
$ pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log start
$ pg_ctl -D /usr/local/var/postgres stop -s -m fast
$ createuser username -P
# Enter password for new role
# Enter it again
$ createdb dbname -O username -E UTF8 -e
$ psql -U username -d dbname -h 127.0.0.1
$ \l
$ \c dbname
$ \d

$ sudo apt-get update
$ sudo apt-get install postgresql postgresql-client
$ sudo /etc/init.d/postgresql start
$ su - postgres
$ psql
$ alter role postgres with password &apos;postgres&apos;

$ CREATE USER user_name WITH PASSWORD &apos;******&apos;;
$ CREATE DATABASE db_name OWNER user_name;
$ GRANT ALL PRIVILEGES ON DATABASE db_name TO user_name;
$ psql -d db_name

$ npm config set registry https://registry.npmjs.org/
$ npm config get registry
$ yarn config set registry https://registry.yarnpkg.com
$ yarn config get registry

$ npm install -g nrm
$ nrm ls
$ nrm use taobao
$ yarn global add yrm
$ yrm ls
$ yrm use taobao

$ npm list -g --depth 0
$ yarn global list

# Will clean all pods
$ pod cache clean --all
# Will remove all installed &apos;FortifySec&apos; pods
$ pod cache clean &apos;FortifySec&apos; --all

$ gem install rails -V

$ sudo apt-get update
$ sudo apt-get upgrade
$ sudo apt-get install postgresql postgresql-client
$ sudo -i -u postgres
$ psql
$ CREATE DATABASE dbName;
$ CREATE USER userName WITH PASSWORD 'password' ; 
$ GRANT ALL PRIVILEGES ON DATABASE dbName to userName;
$ \q
$ psql -d dbname

$ sudo docker run -itd --name imqzzZ -p 16666-16888:16666-16888/tcp ubuntu /bin/bash

$ scp /local_path/test.txt root@47.115.52.51:/home/imzqqq/workspace
$ scp root@47.115.52.51:/home/imzqqq/workspace/world.txt /local_path
$ scp -r /local_folder root@47.115.52.51:/home/imzqqq/workspace/folder
```