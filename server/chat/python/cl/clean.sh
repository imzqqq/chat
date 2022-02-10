#!/usr/bin/env bash

set -e

DIR="$( cd "$( dirname "$0" )" && pwd )"

PID_FILE="$DIR/servers.pid"

if [ -f $PID_FILE ]; then
    echo "servers.pid exists!"
    exit 1
fi

for port in 8080 8081 8082; do
    echo "Removing $DIR/demo/$port..."
    rm -rf $DIR/demo/$port
    echo "Removing $DIR/demo/media_store.$port..."
    rm -rf $DIR/demo/media_store.$port
done


echo "Removing $DIR/etc..."
rm -rf $DIR/etc
echo "Removing $DIR/demo..."
rm -rf $DIR/demo
