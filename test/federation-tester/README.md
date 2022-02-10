Federation Tester
========================

Checks that federation is correctly configured on a Chat server.

Building
--------

The tester is written in [golang](https://golang.org/) 1.12+.

```bash
go build
```

Running
-------

```bash
BIND_ADDRESS=:8085 ./federation-tester
```

Using
-----

The federation tester may be accessed using the following templated URL. Please replace `<server_name>` with server name (eg: `localhost:8085`).

```txt
http://localhost:8085/api/report?server_name=<server_name>
```
