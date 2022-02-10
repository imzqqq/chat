#!/bin/bash

set -e

yarn=tyarn

rm -rf node_modules

pushd megalodon
rm -rf node_modules
rm -rf lib
$yarn unlink &>/dev/null || true
$yarn link
$yarn install
popd

echo "Linking local deps..."
### MARK - imzqqq, FIXME:
$yarn link megalodon
$yarn install
