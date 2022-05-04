# Flow Desktop

The new beautiful, fluffy client for the fediverse written in TypeScript and React

Socialize and communicate with your friends in the fediverse (ActivityPub-powered social networks like Mastodon and Pleroma) with Flow Desktop.
Browse your timelines, check in with friends, and share your experiences across the fediverse in a beautiful, clean, and customizable way.

What Flow Desktop offers:

- A clean, responsive, and streamlined design that fits in with your Mac
- Support for switching between accounts to access the accounts you use the most
- Customization support, ranging from several beautiful themes to masonry layout and infinite scrolling
- Powerful toot composer with media uploads, emojis, and polls
- Activity and recommended views that give you insight on the community/instance you reside in

## webpack

> see <https://segmentfault.com/a/1190000006178770> for more details

- webpack -> `npm install --save-dev webpack`
- `npm install --save-dev  cross-env`
- `npm install --save-dev webpack-cli`
- dev-server -> `npm install --save-dev webpack-dev-server`
- babel -> `npm install --save-dev babel-core babel-loader babel-preset-env babel-preset-react`
- css -> `npm install --save-dev style-loader css-loader`
- post-css -> `npm install --save-dev postcss-loader autoprefixer`
- html-webpack-plugin -> `npm install --save-dev html-webpack-plugin`
- react-transform-hmr -> `npm install --save-dev babel-plugin-react-transform react-transform-hmr`
- extract-text-webpack-plugin -> `npm install --save-dev extract-text-webpack-plugin`
- clean-webpack-plugin -> `npm install clean-webpack-plugin --save-dev`

## Get started

Flow Desktop is available for the major desktop platforms via our downloads page, GitHub, and other store platforms where applicable.

## Build from source

To build Flow Desktop, you'll need the following tools and packages:

- Node.js v10 or later
- (macOS-only) Xcode 10 or higher

### Installing dependencies

In the app directory, run the following command to install all of the package dependencies:

```sh
yarn setup:deps
# formula: Increase to X GB
export NODE_OPTIONS="--max-old-space-size=(X * 1024)" 

yarn start
```

Before building, make sure you set the location field in `public/config.json` to "desktop" before continuing.
To develop locally, modify the location field in your `config.json` to use <https://localhost:3001> as the address.

### Testing changes

Run any of the following scripts to test:

- `yarn start` - Starts a local server hosted at <https://localhost:3001>.
- `yarn run electron:build` - Builds a copy of the source code and then runs the app through Electron. Ensure that the `location` key in `config.json` points to `"desktop"` before running this.
- `yarn run electron:prebuilt` - Similar to `electron:build` but doesn't build the project before running.

The `location` key in `config.json` can take the following values during testing:

- **https://localhost:3001**: Most suitable for running `yarn start` or running via `react-scripts`.
- **desktop**: Most suitable for when testing the desktop application.

> Note: Flow Desktop v1.1.0-beta3 and older versions require the location field to be changed to `"https://localhost:3001"` before running.

### Building a release

To build a release, run the following command:

```sh
yarn run build
```

The built files will be available under `build` as static files that can be hosted on a web server. If you plan to release these files alongside the desktop apps, compress these files in a ZIP.

#### Building desktop apps

You can run any of the following commands to build a release for the desktop:

- `yarn run build:desktop-all`: Builds the desktop apps for all platforms (eg. Windows, macOS, Linux). Will run `yarn run build` before building.
- `yarn run build:win`: Builds the desktop app for Windows without running `yarn run build`.
- `yarn run build:mac`: Builds the desktop apps for macOS without running `yarn run build`. See the details below for more information on building for macOS.
- `yarn run build:mas`: Builds the desktop apps for the Mac App Store without running `yarn run build`. See the details below for more information on building for macOS.
- `yarn run build:linux`: Builds the desktop apps for Linux (eg. Debian package, AppImage, and Snap) without running `yarn run build`.
- `yarn run build:linux-select-targets`: Builds the desktop app for Linux without running `yarn run build`. _Targets are required as parameters._

The built files will be available under `dist` that can be uploaded to your app distributor or website.

#### Extra steps for macOS

The macOS builds of Flow Desktop require a bit more effort and resources to build and distribute accordingly. The following is a quick guide to building Flow Desktop for macOS and for the Mac App Store.

##### Gather your tools

To create a code-signed and notarized version of Flow Desktop, you'll need to acquire some provisioning profiles and certificates from a valid Apple Developer account.

For certificates, make sure your Mac has the following certificates installed:

- 3rd Party Mac Developer Application
- 3rd Party Mac Developer Installer
- Developer ID Application
- Developer ID Installer
- Mac Developer

The easiest way to handle this is by opening Xcode and going to **Preferences &rsaquo; Accounts** and create the certificates from "Manage Certificates".

You'll also need to [create a provisioning profile for **Mac App Store** distribution](https://developer.apple.com/account/resources/profiles/add) and save it to the `desktop` folder as `embedded.provisonprofile`.

##### Create your entitlements files

You'll also need to create the entitlements files in the `desktop` directory that declares the permissions for Flow Desktop. Replace `TEAM_ID` with the appropriate Apple Developer information and `BUNDLE_ID` with the bundle ID of your app.

###### entitlements.mac.plist

```plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
  <dict>
    <key>com.apple.security.cs.allow-unsigned-executable-memory</key>
    <true/>
    <key>com.apple.security.network.client</key>
    <true/>
    <key>com.apple.security.files.user-selected.read-write</key>
    <true/>
  </dict>
</plist>
```

###### entitlements.mas.plist

```plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>com.apple.security.cs.allow-jit</key>
	<true/>
	<key>com.apple.security.network.client</key>
	<true/>
	<key>com.apple.security.app-sandbox</key>
	<true/>
	<key>com.apple.security.cs.allow-unsigned-executable-memory</key>
	<true/>
	<key>com.apple.security.application-groups</key>
	<array>
		<string>TEAM_ID.BUNDLE_ID</string>
	</array>
	<key>com.apple.security.files.user-selected.read-only</key>
	<true/>
	<key>com.apple.security.files.user-selected.read-write</key>
	<true/>
</dict>
</plist>
```

###### entitlements.mas.inherit.plist

```plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
	<dict>
	<key>com.apple.security.app-sandbox</key>
	<true/>
	<key>com.apple.security.inherit</key>
	<true/>
	<key>com.apple.security.cs.allow-jit</key>
	<true/>
	<key>com.apple.security.cs.allow-unsigned-executable-memory</key>
	<true/>
	</dict>
</plist>
```

###### entitlements.mas.loginhelper.plist

```plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
  <dict>
    <key>com.apple.security.app-sandbox</key>
    <true/>
  </dict>
</plist>
```

###### info.plist

```plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>ElectronTeamID</key>
	<string>TEAM_ID</string>
	<key>com.apple.developer.team-identifier</key>
	<string>TEAM_ID</string>
	<key>com.apple.application-identifier</key>
	<string>TEAM_ID.BUNDLE_ID</string>
</dict>
</plist>
```

##### Edit `notarize.js`

You'll also need to edit `notarize.js` in the `desktop` directory. Replace `<TEAM_ID>`, `<BUNDLE_ID>`, and `<APPLE_DEVELOPER_EMAIL>` with the appropriate information from the app and your account from Apple Developer.

```js
// notarize.js
// Script to notarize Flow for macOS

const { notarize } = require("electron-notarize");

// This is pulled from the Apple Keychain. To set this up,
// follow the instructions provided here:
// https://github.com/electron/electron-notarize#safety-when-using-appleidpassword
const password = `@keychain:AC_PASSWORD`;

exports.default = async function notarizing(context) {
    const { electronPlatformName, appOutDir } = context;
    if (electronPlatformName !== "darwin") {
        return;
    }

    console.log("Notarizing Flow...");

    const appName = context.packager.appInfo.productFilename;

    return await notarize({
        appBundleId: "<BUNDLE_ID>",
        appPath: `${appOutDir}/${appName}.app`,
        appleId: "<APPLE_DEVELOPER_EMAIL>",
        appleIdPassword: password,
        ascProvider: "<TEAM_ID>"
    });
};
```

Note that the password is pulled from your keychain. You'll need to create an app password and store it in your keychain as `AC_PASSWORD`.

##### Build the apps

Run any of the following commands to build Flow Desktop for the Mac:

- `yarn run build:mac` - Builds the macOS app in a DMG container.
- `yarn run build:mac-unsigned` - Similar to `build:mac`, but skips code signing and notarization. **Use only for CI or in situations where code signing and notarization is not available.**
- `yarn run build:mas` - Builds the Mac App Store package.
