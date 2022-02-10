#!/bin/bash

set -e

name=$(grep '"name"' package.json | head -n 1 | sed 's|.*: \"\(.*\)\",|\1|')

if [ -n "$DIST_VERSION" ]; then
    version=$DIST_VERSION
else
    version=$(grep version package.json | sed 's|.*: \"\(.*\)\",|\1|')
fi

yarn clean
yarn build

# include the sample config in the tarball. Arguably this should be done by
# `yarn build`, but it's just too painful.
cp config.sample.json webapp/

mkdir -p dist
cp -r webapp $name-$version

# Just in case you have a local config, remove it before packaging
rm $name-$version/config.json || true

# if $version looks like semver with leading v, strip it before writing to file
if [[ ${version} =~ ^v[[:digit:]]+\.[[:digit:]]+\.[[:digit:]]+(-.+)?$ ]]; then
    echo ${version:1} > $name-$version/version
else
    echo ${version} > $name-$version/version
fi

tar chvzf dist/$name-$version.tar.gz $name-$version
rm -r $name-$version

echo
echo "Packaged dist/$name-$version.tar.gz"
