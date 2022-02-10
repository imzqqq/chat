# Fast Run

> DO NOT USE THESE DEMO SERVERS IN PRODUCTION

Requires you to have done: `python setup.py develop`

## Start

The `cl/start.sh` will start three servers on ports 8080, 8081 and 8082, with host names `localhost:$port`. This can be easily changed to `hostname:$port` in `start.sh` if required.

> To enable the servers to communicate untrusted ssl certs are used. In order to do this the servers do not check the certs
and are configured in a highly insecure way. Do not use these configuration files in production.

- `stop.sh` will stop the servers and the webclient.

- `clean.sh` will delete the databases and log files.

To start a completely new set of servers, run:

    ./cl/stop.sh; ./cl/clean.sh && ./cl/start.sh

Logs and sqlitedb will be stored in `cl/808{0,1,2}.{log,db}`

Also note that when joining a public room on a differnt HS via "#foo:bar.net", then you are (in the current impl) joining a room with room_id "foo". This means that it won't work if your HS already has a room with that name.
