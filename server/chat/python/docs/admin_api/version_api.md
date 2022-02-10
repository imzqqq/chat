# Version API

This API returns the running Chat server version and the Python version
on which Chat server is being run. This is useful when a Chat server instance
is behind a proxy that does not forward the 'Server' header (which also
contains Chat server version information).

The api is:

```
GET /_chat/admin/v1/server_version
```

It returns a JSON body like the following:

```json
{
    "server_version": "0.99.2rc1 (b=develop, abcdef123)",
    "python_version": "3.6.8"
}
```
