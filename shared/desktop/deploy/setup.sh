#!/bin/bash

set -e

# yarn=yarnpkg
yarn=yarn

### MARK - imzqqq
OSTYPE="Linux"
if [ "$(uname)" == "Darwin" ]; then
	echo "OS -> Darwin"
	OSTYPE="Macintosh"
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
	echo "OS -> GNU/Linux"
	OSTYPE="Linux"
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
	echo "OS -> Windows"
	OSTYPE="Windows"
fi

pushd ../../olm
file_olm_js="./javascript/olm.js"
if [ ! -f "$file_olm_js" ]
then
    echo "Building OLM JavaScript bindings..."
	
	if [[ $OSTYPE == "Windows" ]]; then
		### MARK - imzqqq, FIXME: 
		echo "Building OLM JavaScript on Windows is not supported now, using prebuilded pkg..."
		### END
	else
		make js
	fi
else
    echo "OLM JavaScript bindings already exists!"
fi
popd

pushd eslint-plugin-matrix-org
$yarn unlink &>/dev/null || true
$yarn link
$yarn install
popd

pushd matrix-widget-api
$yarn unlink &>/dev/null || true
$yarn link
$yarn install
$yarn build
popd

pushd seshat
pushd seshat-node
$yarn unlink &>/dev/null || true
$yarn link
### MARK - imzqqq, FIXME: 
$yarn install
### END
popd
popd
### END

pushd matrix-js-sdk
echo "Linking local deps in matrix-js-sdk..."
$yarn link eslint-plugin-matrix-org
$yarn unlink &>/dev/null || true
$yarn link
$yarn install
popd

pushd matrix-react-sdk
echo "Linking local deps in matrix-react-sdk..."
$yarn link matrix-js-sdk
$yarn link matrix-widget-api
$yarn link eslint-plugin-matrix-org
$yarn unlink &>/dev/null || true
$yarn link
$yarn install
popd

pushd element-web
echo "Linking local deps in element-web..."
$yarn link matrix-js-sdk
$yarn link matrix-react-sdk
$yarn link matrix-widget-api
$yarn link eslint-plugin-matrix-org
$yarn install
popd

pushd element-desktop
echo "Linking local deps in element-desktop..."
$yarn install
ln -s ../element-web/webapp ./ || true
popd

pushd i18n-helper
$yarn install
popd
