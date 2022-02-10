#!/bin/sh
set -e


find /config ! -user tube -exec chown tube:tube {} \; || true

# first arg is `-f` or `--some-option`
# or first arg is `something.conf`
if [ "${1#-}" != "$1" ] || [ "${1%.conf}" != "$1" ]; then
    set -- npm "$@"
fi

# allow the container to be started with `--user`
if [ "$1" = 'npm' -a "$(id -u)" = '0' ]; then
    find /data ! -user tube -exec  chown tube:tube {} \;
    exec gosu tube "$0" "$@"
fi

exec "$@"
