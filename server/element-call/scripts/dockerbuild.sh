#!/bin/sh

set -ex

export VITE_DEFAULT_HOMESERVER=https://call.ems.host
export VITE_SENTRY_DSN=https://b1e328d49be3402ba96101338989fb35@sentry.matrix.org/41
export VITE_RAGESHAKE_SUBMIT_URL=https://element.io/bugreports/submit
export VITE_PRODUCT_NAME="Element Call"

git clone https://github.com/matrix-org/matrix-js-sdk.git
cd matrix-js-sdk
git checkout robertlong/group-call
yarn install
yarn run build
yarn link

cd ../element-call

export VITE_APP_VERSION=$(git describe --tags --abbrev=0)

yarn link matrix-js-sdk
yarn install
yarn run build
