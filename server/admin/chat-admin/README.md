# Chat Server Admin

This project is built using [react-admin](https://marmelab.com/react-admin/).

It needs at least Chat Server v1.38.0 for all functions to work as expected!

You get your server version with the request `/_chat/admin/v1/server_version`.

After entering the URL on the login page of Chat server-admin the server version appears below the input field.

You need access to the following endpoints:

- `/chat`
- `/_chat/admin`

## Step-By-Step install

You have three options:

1. Download the tarball and serve with any webserver
2. Download the source code from github and run using nodejs
3. Run the Docker container

Steps for 1):

- make sure you have a webserver installed that can serve static files (any webserver like nginx or apache will do)
- configure a vhost for Chat server admin on your webserver
- download the .tar.gz from the latest release
- unpack the .tar.gz
- move or symlink the `server-admin-x.x.x` into your vhosts root dir
- open the url of the vhost in your browser

Steps for 2):

- make sure you have installed the following: git, yarn, nodejs
- download the source code
- change into downloaded directory: `cd server-admin`
- download dependencies: `yarn install`
- start web server: `yarn start`

You can fix the homeserver, so that the user can no longer define it himself.
Either you define it at startup (e.g. `REACT_APP_SERVER=https://your-server.example.com yarn start`)
or by editing it in the [.env](.env) file. See also the
[documentation](https://create-react-app.dev/docs/adding-custom-environment-variables/).

Steps for 3):

- run the Docker container from the public docker registry: `docker run -p 3001:80 imzqqq/server-admin` or use the [docker-compose.yml](docker-compose.yml): `docker-compose up -d`

  > note: if you're building on an architecture other than amd64 (for example a raspberry pi), make sure to define a maximum ram for node. otherwise the build will fail.

  ```yml
  version: "3"

  services:
    server-admin:
      container_name: server-admin
      hostname: server-admin
      image: imzqqq/server-admin:latest

      # build:
        # context: https://github.com/imzqqq
        # args:
        #   - NODE_OPTIONS="--max_old_space_size=1024"
      ports:
        - "3001:80"
      restart: unless-stopped
  ```

- Browse to `http://localhost:3001`

## Development

- Use `yarn test` to run all style, lint and unit tests
- Use `yarn fix` to fix the coding style

## Production

- Run command `yarn build`
