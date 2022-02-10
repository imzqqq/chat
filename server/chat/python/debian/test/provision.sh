#!/bin/bash
#
# provisioning script for vagrant boxes for testing the chat-server debs.
#
# Will install the most recent chat-server-py3 deb for this platform from
# the /debs directory.

set -e

apt-get update
apt-get install -y lsb-release

deb=`ls /debs/chat-server-py3_*+$(lsb_release -cs)*.deb | sort | tail -n1`

debconf-set-selections <<EOF
chat-server chat-server/report-stats boolean false
chat-server chat-server/server-name string localhost:18448
EOF

dpkg -i "$deb"

sed -i -e '/port: 8...$/{s/8880/18448/; s/8080/18008/}' -e '$aregistration_shared_secret: secret' /etc/chat-server/homeserver.yaml
systemctl restart chat-server
