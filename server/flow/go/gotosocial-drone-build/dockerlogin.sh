#!/bin/sh

set -eu

DOCKER_PASSWORD="${DOCKER_PASSWORD:-$(cat dockerpassword)}"
DOCKER_USERNAME="${DOCKER_USERNAME:-$(cat dockerusername)}"

rc-update add docker boot

echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
