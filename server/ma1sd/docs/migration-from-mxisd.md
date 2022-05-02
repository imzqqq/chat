# Migration from mxisd

Version 2.0.0 of the ma1sd uses the same format of the database schema and main configuration file as mxisd.

Migration from mxisd:
- install ma1sd via deb package, docker image or zip/tar archive
- stop mxisd
- copy configuration file (by default /etc/mxisd/mxisd.yaml to /etc/ma1sd/ma1sd.yaml)
- copy key store (by default /var/lib/mxisd/keys folder to /var/lib/ma1sd/keys)
- copy storage (by default /var/lib/mxisd/store.db to /var/lib/ma1sd/store.db)
- change paths in the new config file (ma1sd.yaml). There are options: `key.path` and `storage.provider.sqlite`
- start ma1sd

Due to ma1sd uses the same ports by default as mxisd it isn't necessary to change nginx/apache configuration.

If you have any troubles with migration don't hesitate to ask questions in [#ma1sd:ru-matrix.org](https://matrix.to/#/#ma1sd:ru-matrix.org) room.
