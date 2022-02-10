#!/usr/bin/env bash

# 1.Install denpendencies
python3 -m venv ./.env
source ./.env/bin/activate

python -m pip install -e ../canonicaljson
python -m pip install -e ../chat-ldap3
python -m pip install -e ".[all,lint,mypy,test]"

# Test tool
python -m pip install tox

# 1.Create the homeserver.yaml config once
python3 -m chat.app.homeserver --server-name chat.dingshunyu.top --config-path homeserver.yaml --generate-config --report-stats=yes

# 2.Start the app
python3 -m chat.app.homeserver --config-path homeserver.yaml
