#!/usr/bin/env bash

# 1.venv
echo "Creating virtual enviroment..."
python3 -m venv ./.env
# FIXME: this not working when running with bash
source ./.env/bin/activate

# 2.Install dependencies
echo "Trying to install all dependencies..."
pip3 install -e ".[all,test]"

# 3.Run unit tests to check that everything is installed correctly
echo "Checking all dependencies are installed correctly..."
python3 -m twisted.trial tests

echo "Everything is ready, let's enjoy!"
