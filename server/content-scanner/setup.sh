#!/bin/bash

set -e

yarn=yarnpkg

### MARK - Build local olm and seshat package
pushd ../olm
file_olm_js="./javascript/olm.js"
if [ ! -f "$file_olm_js" ]
then
    echo "Building OLM JavaScript bindings..."
    make js
else
    echo "OLM JavaScript bindings already exists!"
fi
popd
###

$yarn install
