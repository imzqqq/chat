#!/usr/bin/env bash

yarn install --no-package-lock

rm -rf vendor/ && mkdir vendor/

cp node_modules/docsify/lib/docsify.min.js vendor/
cp node_modules/docsify/lib/plugins/external-script.min.js vendor/
cp node_modules/docsify/lib/plugins/matomo.min.js vendor/
cp node_modules/docsify/lib/plugins/emoji.min.js vendor/

cp node_modules/prismjs/themes/prism.css vendor/
cp node_modules/prismjs/components/prism-bash.min.js vendor/
cp node_modules/prismjs/components/prism-json.min.js vendor/
cp node_modules/prismjs/components/prism-typescript.min.js vendor/
cp node_modules/prismjs/components/prism-yaml.min.js vendor/
cp node_modules/prismjs/components/prism-css.min.js vendor/

cp node_modules/feather-icons/dist/feather.min.js vendor/

cp node_modules/redoc/bundles/* vendor/
