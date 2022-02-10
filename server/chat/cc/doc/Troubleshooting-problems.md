# TROUBLESHOOTING

- [Useful program options](#Useful-program-options)
- [Recovering from broken configurations](#Recovering-from-broken-configurations)
- [Recovering from database corruption](#Recovering-from-database-corruption)
- [Trouble with reverse proxies and middlewares](#Trouble-with-reverse-proxies-and-middlewares)

***

##### Useful program options

Start the daemon with one or more of the following program options to make it
easier to troubleshoot and perform maintenance:

- `-debug` increases the logging level to the terminal.

- `-single` will start in "single user mode" which is a convenience combination
of `-nolisten -wa -console` options described below.

- `-safe` is more restrictive than `-single` by denying outbound network requests and background database tasks.

- `-nolisten` will disable the loading of any listener sockets during startup.

- `-nobackfill` specifically skips the initial-backfill background task.

- `-wa` write-avoid will discourage (but not deny) writes to the database. This prevents a lot of background tasks and other noise, making it easier to conduct maintenance (implies `-nobackfill`).

- `-ro` read-only is more restrictive than `-wa` by denying all writes to the database.

- `-slave` allows read-only access to a live database by additional instances of Construct. Only one instance of Construct may have write access to a database at a time; additional instances use this option.

- `-console` convenience to immediately drop to the adminstrator console
after startup.

##### Recovering from broken configurations

If your server ever fails to start from an errant conf item: you can override
any item using an environmental variable before starting the program. To do
this simply replace the '.' characters with '_' in the name of the item when
setting it in the environment. The name is otherwise the same, including its
lower case.

Otherwise, the program can be run with the option `-defaults`. This will
prevent initial loading of the configuration from the database. It will
not prevent environmental variable overrides (as mentioned above). Values
will not be written back to the database unless they are explicitly set by
the user in the console.


##### Recovering from database corruption

In very rare cases after a hard crash the journal cannot completely restore
data before the crash. Due to the design of rocksdb and the way we apply it
for Matrix, data is lost in chronological order starting from the most recent
transaction (matrix event). The database is consistent for all events up until
the first corrupt event, called the point-in-time.

When any loss has occurred the daemon will fail to start normally. To enable
point-in-time recovery use the command-line option `-recoverdb point` at the next
invocation. Some recent events may be lost. If `-recoverdb point` does not work,
others techniques may be invoked as detailed below. Additional program options such
as `-single -ro` may be advisable for some salvage techniques.

```
-recoverdb <option>
```
- 🟢 **point** - Recovery mode; rewinds the database to the last consistent state before corruption.
- 🔴 **skip** -  Salvage mode; drops recent corrupt data, which will leave the database in an inconsistent state.
- 🔴 **repair** - Salvage mode; finds and drops deep corruption. This will leave the database in an inconsistent state.
- 🔴 **recover** - Salvage mode; expert use only. Data may be lost.
- 🔴 **tolerate** - Salvage mode; expert use only.

##### Trouble with reverse proxies and middlewares

Construct is designed to be capable internet service software and should
perform best when directly interfacing with remote parties. Nevertheless,
some users wish to employ middlewares known as "reverse-proxies" through
which all communication is forwarded. This gives the appearance, from the
server's perspective, that all clients are connecting from the same IP
address on different ports.

At this time there are some known issues with reverse proxies which may be
mitigated by administrators having reviewed the following:

1. The connection limit from a single remote IP address must be raised from
its default, for example by entering the following in !control or console:

```
conf set ircd.client.max_client_per_peer 65535
```

2. The server does not yet support non-SSL listening sockets. Administrators
may have to generate locally signed certificates for communication from the
reverse-proxy.

3. Ensure the reverse-proxy is not rewriting the `Host:` header which is sent to Construct. The header must appear exactly as sent by remote clients.

4. Ensure the reverse-proxy is not setting `Connection: close` when communicating to Construct. The ideal middleware is configured to maintain a pool of persistent connections and pipeline requests. As a hint based on Construct's default settings at the time of this writing, the optimal connection count from the middleware is 64, and up to 128.
