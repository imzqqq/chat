#!/usr/bin/env bash

# This script builds the Docker image to run the PostgreSQL tests, and then runs
# the tests.

set -e

# Build, and tag
docker build docker/ -f docker/Dockerfile-pgtests -t pgtests

# Run, mounting the current directory into /src
# --rm: Automatically remove the container when it exits
docker run --rm -it -v $(pwd)\:/src pgtests
