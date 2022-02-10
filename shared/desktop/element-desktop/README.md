# Warning: This information is only partially applicable to Chat, look [Desktop](../README.md) instead

<pre><code><a>desktop</a> (recommended starting point to build for Web <b>and</b> Desktop)
|-- <b>element-desktop</b> <i>&lt;-- this repo</i> (electron wrapper)
|-- <a>element-web</a> ("skin" for matrix-react-sdk)
|-- <a>matrix-react-sdk</a> (most of the development happens here)
`-- <a>matrix-js-sdk</a> (Chat client js sdk)
</code></pre>

## Desktop

Desktop is a Chat client for desktop platforms with Chat Web at its core.

## First Steps

Before you do anything else, fetch the dependencies:

```bash
yarn install
```

## Fetching Client

Since this package is just the Electron wrapper for Chat Web, it doesn't contain any of the Chat Web code,
so the first step is to get a working copy of Chat Web. There are a few ways of doing this:

```bash
# Fetch the prebuilt release Chat package from the element-web GitHub releases page. The version
# fetched will be the same as the local element-desktop package.
# We're explicitly asking for no config, so the packaged Chat will have no config.json.
yarn run fetch --noverify --cfgdir ""
```

...or if you'd like to use GPG to verify the downloaded package:

```bash
# Fetch the Chat public key from the element.io web server over a secure connection and import
# it into your local GPG keychain (you'll need GPG installed). You only need to to do this
# once.
yarn run fetch --importkey
# Fetch the package and verify the signature
yarn run fetch --cfgdir ""
```

...or either of the above, but fetching a specific version of Chat:

```bash
# Fetch the prebuilt release Chat package from the element-web GitHub releases page. The version
# fetched will be the same as the local element-desktop package.
yarn run fetch --noverify --cfgdir "" v1.5.6
```

If you only want to run the app locally and don't need to build packages, you can
provide the `webapp` directory directly:

```bash
# Assuming you've checked out and built a copy of element-web in ../element-web
ln -s ../element-web/webapp ./
```

[TODO: add support for fetching develop builds, arbitrary URLs and arbitrary paths]

## Building

Now you have a copy of Chat, you're ready to build packages. If you'd just like to
run Chat locally, skip to the next section.

If you'd like to build the native modules (for searching in encrypted rooms and
secure storage), do this first. This will take 10 minutes or so, and will
require a number of native tools to be installed, depending on your OS (eg.
rust, tcl, make/nmake).

You'll also to need to make sure you've built the native modules for the same
architecture as your package, so for anything more advanced than just building
the modules and app for the host architecture see 'Other Architectures'.

If you don't need these features, you can skip this step.

To just build these for your native architecture:

```bash
yarn run build:native
```

Now you can build the package:

```bash
yarn run build
```

This will do a couple of things:

* Run the `setversion` script to set the local package version to match whatever
   version of Chat you installed above.
* Run electron-builder to build a package. The package built will match the operating system
   you're running the build process on.

This build step will not build any native modules.

You can also build using docker, which will always produce the linux package:

```bash
# Run this once to make the docker image
yarn run docker:setup

yarn run docker:install
# if you want to build the native modules (this will take a while)
yarn run docker:build:native
yarn run docker:build
```

After running, the packages should be in `dist/`.

## Starting

If you'd just like to run the electron app locally for development:

```bash
# Install electron - we don't normally need electron itself as it's provided
# by electron-builder when building packages
yarn add electron
yarn start
```

## Other Architectures

Building the native modules will build for the host architecture (and only the
host architecture) by default. On Windows, this will automatically determine
the architecture to build for based on the environment. Make sure that you have
all the [tools required to perform the native modules build](docs/windows-requirements.md)

On macOS, you can build universal native modules too:

```bash
yarn run build:native:universal
```

...or you can build for a specific architecture:

```bash
yarn run build:native --target x86_64-apple-darwin
```

or

```bash
yarn run build:native --target aarch64-apple-darwin
```

You'll then need to create a built bundle with the same architecture.
To bundle a universal build for macOS, run:

```bash
yarn run build:universal
```

If you're on Windows, you can choose to build specifically for 32 or 64 bit:

```bash
yarn run build:32
```

or

```bash
yarn run build:64
```

Note that the native module build system keeps the different architectures
separate, so you can keep native modules for several architectures at the same
time and switch which are active using a `yarn run hak copy` command, passing
the appropriate architectures. This will error if you haven't yet built those
architectures. eg:

```bash
yarn run build:native --target x86_64-apple-darwin
# We've now built & linked into place native modules for Intel
yarn run build:native --target aarch64-apple-darwin
# We've now built Apple Silicon modules too, and linked them into place as the active ones

yarn run hak copy --target x86_64-apple-darwin
# We've now switched back to our Intel modules
yarn run hak copy --target x86_64-apple-darwin --target aarch64-apple-darwin
# Now our native modules are universal x86_64+aarch64 binaries
```

The current set of native modules are stored in `.hak/hakModules`,
so you can use this to check what architecture is currently in place, eg:

```bash
$ lipo -info .hak/hakModules/keytar/build/Release/keytar.node 
Architectures in the fat file: .hak/hakModules/keytar/build/Release/keytar.node are: x86_64 arm64 
```

## Config

If you'd like the packaged Chat to have a configuration file, you can create a
config directory and place `config.json` in there, then specify this directory
with the `--cfgdir` option to `yarn run fetch`, eg:

```bash
mkdir myconfig
cp /path/to/my/config.json myconfig/
yarn run fetch --cfgdir myconfig
```

The config dir for the official Chat app is in `element.io`. If you use this,
your app will auto-update itself using builds from element.io.

## Profiles

To run multiple instances of the desktop app for different accounts, you can
launch the executable with the `--profile` argument followed by a unique
identifier, e.g `element-desktop --profile Work` for it to run a separate profile and
not interfere with the default one.

Alternatively, a custom location for the profile data can be specified using the
`--profile-dir` flag followed by the desired path.

## User-specified config.json

* `%APPDATA%\$NAME\config.json` on Windows
* `$XDG_CONFIG_HOME\$NAME\config.json` or `~/.config/$NAME/config.json` on Linux
* `~/Library/Application Support/$NAME/config.json` on macOS

In the paths above, `$NAME` is typically `Chat`, unless you use `--profile
$PROFILE` in which case it becomes `Chat-$PROFILE`, or it is using one of
the above created by a pre-1.7 install, in which case it will be `Classic_Chat` or
`Classic_Chat-$PROFILE`.

`file:../seshat/seshat-node`
