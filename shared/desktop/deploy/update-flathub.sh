#!/bin/bash

set -e
# set -x

DEPLOY_ROOT="$(dirname "$(realpath "$0")")"

version="$1"
debpath="$2"

repopath="$DEPLOY_ROOT/repos/flathub"
repourl="git@github.com:flathub/chat.imzqqq.desktop.git"

downloadurl="https://github.com/Chat/chat-desktop/releases/download/v${version}/chat-desktop_${version}_amd64.deb"
sha256sum=($(sha256sum $debpath))
debdate=$(date +%Y-%m-%d -r $debpath)

[ -d "$repopath" ] || git clone $repourl $repopath

pushd "$repopath" > /dev/null

git fetch
git reset --hard origin/master

yamlFile="chat.imzqqq.desktop.yaml"
xmlFile="chat.imzqqq.desktop.metainfo.xml"

sed -i "s|url: .* #SC:url|url: $downloadurl #SC:url|" "$yamlFile"
sed -i "s|sha256: .* #SC:sha256|sha256: $sha256sum #SC:sha256|" "$yamlFile"

sed -i "s|^\s\s<releases>$|  <releases>\n    <release version=\"$version\" date=\"$debdate\"/>|" "$xmlFile"

git add $yamlFile $xmlFile
git commit -m "Bump version to v$version"

git push

popd > /dev/null

echo "Release v$version published on flathub!"
