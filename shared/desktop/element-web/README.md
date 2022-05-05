# Chat Web

> Warning: This information is only partially applicable, look [Desktop](../README.md) instead

<pre><code><a>desktop</a> (recommended starting point to build Web <b>and</b> Desktop)
|-- <a>element-desktop</a> (electron wrapper)
|-- <b>element-web</b> <i>&lt;-- this repo</i> ("skin" for matrix-react-sdk)
|-- <a>matrix-react-sdk</a> (most of the development happens here)
`-- <a>matrix-js-sdk</a> (Chat client js sdk)
</code></pre>

## Supported Environments

Chat has several tiers of support for different environments:

* Supported
  * Definition: Issues **actively triaged**, regressions **block** the release
  * Last 2 major versions of Chrome, Firefox, Safari, and Edge on desktop OSes
  * Latest release of official Chat Desktop app on desktop OSes
  * Desktop OSes means macOS, Windows, and Linux versions for desktop devices
    that are actively supported by the OS vendor and receive security updates
* Experimental
  * Definition: Issues **accepted**, regressions **do not block** the release
  * Chat as an installed PWA via current stable version of Chrome, Firefox, and Safari
  * Mobile web for current stable version of Chrome, Firefox, and Safari on Android, iOS, and iPadOS
* Not supported
  * Definition: Issues only affecting unsupported environments are **closed**
  * Everything else

## Getting Started

Note that for the security of your chats will need to serve Chat
over HTTPS. Major browsers also do not allow you to use VoIP/video
chats over HTTP, as WebRTC is only usable over HTTPS.
There are some exceptions like when using localhost, which is
considered a [secure context](https://developer.mozilla.org/docs/Web/Security/Secure_Contexts)
and thus allowed.

To install chat client as a desktop application, see [Running as a desktop
app](#running-as-a-desktop-app) below.

## Important Security Notes

### Separate domains

We do not recommend running Chat from the same domain name as your Chat
homeserver.  The reason is the risk of XSS (cross-site-scripting)
vulnerabilities that could occur if someone caused Chat to load and render
malicious user generated content from a Chat API which then had trusted
access to Chat (or other apps) due to sharing the same domain.

We have put some coarse mitigations into place to try to protect against this
situation, but it's still not good practice to do it in the first place.  See
<https://github.com/vector-im/element-web/issues/1977> for more details.

### Configuration best practices

Unless you have special requirements, you will want to add the following to
your web server configuration when hosting Chat Web:

* The `X-Frame-Options: SAMEORIGIN` header, to prevent Chat Web from being
  framed and protect from [clickjacking][owasp-clickjacking].
* The `frame-ancestors 'none'` directive to your `Content-Security-Policy`
  header, as the modern replacement for `X-Frame-Options` (though both should be
  included since not all browsers support it yet, see
  [this][owasp-clickjacking-csp]).
* The `X-Content-Type-Options: nosniff` header, to [disable MIME
  sniffing][mime-sniffing].
* The `X-XSS-Protection: 1; mode=block;` header, for basic XSS protection in
  legacy browsers.

[mime-sniffing]:
<https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types#mime_sniffing>

[owasp-clickjacking-csp]:
<https://cheatsheetseries.owasp.org/cheatsheets/Clickjacking_Defense_Cheat_Sheet.html#content-security-policy-frame-ancestors-examples>

[owasp-clickjacking]:
<https://cheatsheetseries.owasp.org/cheatsheets/Clickjacking_Defense_Cheat_Sheet.html>

If you are using nginx, this would look something like the following:

```json
add_header X-Frame-Options SAMEORIGIN;
add_header X-Content-Type-Options nosniff;
add_header X-XSS-Protection "1; mode=block";
add_header Content-Security-Policy "frame-ancestors 'none'";
```

Note: In case you are already setting a `Content-Security-Policy` header
elsewhere, you should modify it to include the `frame-ancestors` directive
instead of adding that last line.

## Building From Source

Chat is a modular webapp built with modern ES6 and uses a Node.js build system.
Ensure you have the latest LTS version of Node.js installed.

Using `yarn` instead of `npm` is recommended. Please see the Yarn [install
guide](https://classic.yarnpkg.com/en/docs/install) if you do not have it already.

1. Install or update `node.js` so that your `node` is at least v10.x.
2. Install `yarn` if not present already.
3. Switch to the element-web directory: `cd element-web`.
4. Install the prerequisites: `yarn install`.
   * If you're using the `develop` branch, then it is recommended to set up a
     proper development environment (see [Setting up a dev
     environment](#setting-up-a-dev-environment) below).
5. Configure the app by copying `config.sample.json` to `config.json` and
   modifying it. See the [configuration docs](docs/config.md) for details.
6. `yarn dist` to build a tarball to deploy. Untaring this file will give
   a version-specific directory containing all the files that need to go on your
   web server.

> If command failed with error: FATAL ERROR: Ineffective mark-compacts near heap limit Allocation failed - JavaScript heap out of memory, run `export NODE_OPTIONS="--max-old-space-size=8192"`

Note that `yarn dist` is not supported on Windows, so Windows users can run `yarn build`,
which will build all the necessary files into the `webapp` directory. The version of Chat
will not appear in Settings without using the dist script. You can then mount the
`webapp` directory on your web server to actually serve up the app, which is
entirely static content.

## Running as a Desktop app

Other options for running as a desktop app:

* @asdf:chat.imzqqq.top points out that you can use nativefier and it just works(tm)

```bash
yarn global add nativefier
nativefier https://app.element.io/
```

The [configuration docs](docs/config.md#desktop-app-configuration) show how to
override the desktop app's default settings if desired.

## Running from Docker

The Docker image can be used to serve element-web as a web server. The easiest way to use
it is to use the prebuilt image:

```bash
docker run -p 80:80 vectorim/element-web
```

To supply your own custom `config.json`, map a volume to `/app/config.json`. For example,
if your custom config was located at `/etc/element-web/config.json` then your Docker command
would be:

```bash
docker run -p 80:80 -v /etc/element-web/config.json:/app/config.json vectorim/element-web
```

To build the image yourself:

```bash
cd element-web
docker build .
```

If you're building a custom branch, or want to use the develop branch, check out the appropriate
element-web branch and then run:

```bash
docker build -t \
    --build-arg USE_CUSTOM_SDKS=true \
    --build-arg REACT_SDK_REPO="https://github.com/matrix-org/matrix-react-sdk.git" \
    --build-arg REACT_SDK_BRANCH="develop" \
    --build-arg JS_SDK_REPO="https://github.com/matrix-org/matrix-js-sdk.git" \
    --build-arg JS_SDK_BRANCH="develop" \
    .
```

## Running in Kubernetes

The provided element-web docker image can also be run from within a Kubernetes cluster.
See the [Kubernetes example](docs/kubernetes.md) for more details.

## config.json

Chat supports a variety of settings to configure default servers, behaviour, themes, etc.
See the [configuration docs](docs/config.md) for more details.

## Labs Features

Some features of Chat may be enabled by flags in the `Labs` section of the settings.
Some of these features are described in [labs.md](https://github.com/vector-im/element-web/blob/develop/docs/labs.md).

## Caching requirements

Chat requires the following URLs not to be cached, when/if you are serving Chat from your own webserver:

```bash
/config.*.json
/i18n
/home
/sites
/index.html
```

## Development

Before attempting to develop on Chat you **must** read the [Developer Guide](../matrix-react-sdk/README.md#developer-guide) for `matrix-react-sdk`,
which also defines the design, architecture and style for Chat too.

Before starting work on a feature, it's best to ensure your plan aligns well
with our vision for Chat. Please chat with the team in
[#element-dev:chat.imzqqq.top](https://to.chat.imzqqq.top/#/#element-dev:chat.imzqqq.top) before you
start so we can ensure it's something we'd be willing to merge.

You should also familiarise yourself with the ["Here be Dragons" guide
](https://docs.google.com/document/d/12jYzvkidrp1h7liEuLIe6BMdU0NUjndUYI971O06ooM)
to the tame & not-so-tame dragons (gotchas) which exist in the codebase.

The idea of Chat is to be a relatively lightweight "skin" of customisations on
top of the underlying `matrix-react-sdk`. `matrix-react-sdk` provides both the
higher and lower level React components useful for building Chat communication
apps using React.

After creating a new component you must run `yarn reskindex` to regenerate
the `component-index.js` for the app (used in future for skinning).

Please note that Chat is intended to run correctly without access to the public
internet.  So please don't depend on resources (JS libs, CSS, images, fonts)
hosted by external CDNs or servers but instead please package all dependencies
into Chat itself.

## Setting up a dev environment

Much of the functionality in Chat is actually in the `matrix-react-sdk` and
`matrix-js-sdk` modules. It is possible to set these up in a way that makes it
easy to track the `develop` branches in git and to make local changes without
having to manually rebuild each time.

First clone and build `matrix-js-sdk`:

``` bash
pushd matrix-js-sdk
yarn link
yarn install
popd
```

Then similarly with `matrix-react-sdk`:

```bash
pushd matrix-react-sdk
yarn link
yarn link matrix-js-sdk
yarn install
popd
```

Finally, build and start Chat itself:

```bash
cd element-web
yarn link matrix-js-sdk
yarn link matrix-react-sdk
yarn install
yarn start
```

Wait a few seconds for the initial build to finish; you should see something like:

```bash
[element-js] <s> [webpack.Progress] 100%
[element-js]
[element-js] ℹ ｢wdm｣:    1840 modules
[element-js] ℹ ｢wdm｣: Compiled successfully.
```

   Remember, the command will not terminate since it runs the web server
   and rebuilds source files when they change. This development server also
   disables caching, so do NOT use it in production.

Configure the app by copying `config.sample.json` to `config.json` and
modifying it. See the [configuration docs](docs/config.md) for details.

Open <http://127.0.0.1:8084/> in your browser to see your newly built Chat.

**Note**: The build script uses inotify by default on Linux to monitor directories
for changes. If the inotify limits are too low your build will fail silently or with
`Error: EMFILE: too many open files`. To avoid these issues, we recommend a watch limit
of at least `128M` and instance limit around `512`.

You may be interested in issues [#15750](https://github.com/vector-im/element-web/issues/15750) and
[#15774](https://github.com/vector-im/element-web/issues/15774) for further details.

To set a new inotify watch and instance limit, execute:

```bash
sudo sysctl fs.inotify.max_user_watches=131072
sudo sysctl fs.inotify.max_user_instances=512
sudo sysctl -p
```

If you wish, you can make the new limits permanent, by executing:

```bash
echo fs.inotify.max_user_watches=131072 | sudo tee -a /etc/sysctl.conf
echo fs.inotify.max_user_instances=512 | sudo tee -a /etc/sysctl.conf
sudo sysctl -p
```

___

When you make changes to `matrix-react-sdk` or `matrix-js-sdk` they should be
automatically picked up by webpack and built.

If you add or remove any components from the Chat skin, you will need to rebuild
the skin's index by running, `yarn reskindex`.

If any of these steps error with, `file table overflow`, you are probably on a mac
which has a very low limit on max open files. Run `ulimit -Sn 1024` and try again.
You'll need to do this in each new terminal you open before building Chat.

### Running the tests

There are a number of application-level tests in the `tests` directory; these
are designed to run in a browser instance under the control of
[karma](https://karma-runner.github.io). To run them:

* Make sure you have Chrome installed (a recent version, like 59)
* Make sure you have `matrix-js-sdk` and `matrix-react-sdk` installed and
  built, as above
* `yarn test`

The above will run the tests under Chrome in a `headless` mode.

You can also tell karma to run the tests in a loop (every time the source
changes), in an instance of Chrome on your desktop, with `yarn
test-multi`. This also gives you the option of running the tests in 'debug'
mode, which is useful for stepping through the tests in the developer tools.

### End-to-End tests

See [matrix-react-sdk](../matrix-react-sdk/README.md#end-to-end-tests) how to run the end-to-end tests.
