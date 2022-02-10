#!/bin/bash

set -e

yarn=yarnpkg

$yarn install

file_env="./.env.default"
echo "Copying environment variables from $file_env to ./.env..."
if [ ! -f "$file_env" ]; then
    cp ./.env.default ./.env
else
    echo "./.env file already exists! Using existing one..."
fi
