### MARK - imzqqq, TODO:

BUILD_DIR = build
CLEANCSS = ./node_modules/.bin/cleancss
DEPLOY_DIR = libs
LIBJITSIMEET_DIR = node_modules/lib-jitsi-meet/
LIBFLAC_DIR = node_modules/libflacjs/dist/min/
OLM_DIR = node_modules/@matrix-org/olm
RNNOISE_WASM_DIR = node_modules/rnnoise-wasm/dist/
TFLITE_WASM = react/features/stream-effects/virtual-background/vendor/tflite
MEET_MODELS_DIR  = react/features/stream-effects/virtual-background/vendor/models/
NODE_SASS = ./node_modules/.bin/sass
NPM = npm
OUTPUT_DIR = .
STYLES_BUNDLE = css/all.bundle.css
STYLES_DESTINATION = css/all.css
STYLES_MAIN = css/main.scss
WEBPACK = ./node_modules/.bin/webpack
WEBPACK_DEV_SERVER = ./node_modules/.bin/webpack-dev-server

all: compile deploy clean

clean:
	rm -fr $(BUILD_DIR)
