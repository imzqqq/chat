---
title: Configuring Flow
layout: doc
description: Customize Flow on an administrative level with config.json.
product: Flow Desktop
---

Newer versions of Flow, starting from 1.0, come with a config.json file in the build folder to configure various parts of Flow without digging into the source code. This config file includes branding, licenses, federation support, and a few other configurations.

> Important: As of Flow 1.0.0beta4, all Boolean keys are no longer wrapped as strings; your config file must have all Boolean values as they are.

## Change Flow's branding

The branding background, logo, and name is controlled under the `branding` key:

```json
 "branding": {
    "name": "Flow",
    "logo": "logo.svg",
    "background": "background.png"
},
```

This key is not particularly required for Flow, though it is generally recommended to keep this key in the file. This key has three subkeys:

| Subkey | Default value | Description |
| ------- | -------------- | ------------------------ |
| `name` | "Flow" | The name of the app or of the brand. Affects the app name in the title bar, About page, and other parts what use Flow as the name. |
| `logo` | "logo.svg" | The location, (relative to the build files), of the logo used on the login screen. |
| `background` | "background.png" | The location, (relative to the build files), of the background used on the login screen. |

## Manage instance information

There are some extra keys for handling actions such as federation, registrations, and instance administrators. There are a few keys in the config file to handle this:

 | Key | Default value | Description |
 | ------- | --------- | ------------------------ |
| `federated` | n/a | Whether the instance of Mastodon is federated or not. This is used to disable access to public fediverse features in Flow for local instances. |
| `registration` | n/a | Information regarding registration of the instance. See below for more information |
| `admin` | n/a | The administrator that installed Flow. |

> Note: As of [Flow 1.0.0beta4](https://github.com/hyperspacedev/hyperspace/releases/tag/1.0.0beta4)+, the `federated` key now expands to more configuration settings.

### Registration information

 | Subkey | Default value | Description |
 | --- | --- | ------------------------ |
| `defaultInstance` | "mastodon.social" | The domain name associated with the Mastodon instance that Flow will connect to by default. This is used to handle well-known logins and direct users to the correct registration page. |

> Important: As of Flow 1.0.0beta7, this key will automatically reset to "mastodon.social" if it is set to a disallowed domain.

### Admin information

| Subkey | Default value | Description |
| --- | --- | ------------------------
| `name` | n/a | The name of the administrator. This can be a display name or real name. |
| `account` | n/a | The account ID number on the Mastodon network defined in `defaultInstance`. |

### Federation information

| Subkey | Default value | Description |
| --- | --- | ------------------------
| `universalLogin` | true | Whether anyone from any Mastodon instance can login to the app |
| `allowPublicPosts` | true | Whether users can post publicly on the app |
| `enablePublicTimeline` | true | Whether users can view the public timeline |

### Example

```json
"federated": {
    "universalLogin": true,
    "allowPublicPosts": true,
    "enablePublicTimeline": true
},
"registration": {
    "defaultInstance": "mastodon.social"
},
"admin": {
    "name": "Eugen",
    "account": "1"
},
```

## Configure app information

There are other fields responsible for handling app information such as version, developer mode, licensing, and source code.

| Key | Default value | Description |
| --- | --- | ------------------------
| `developer` | false | Whether the app should be run in developer mode or not. This helps distinguish between a stable release and a beta release for users. |
| `version` | Flow version as a string | **Required:** The version of the application. |
| `license` | n/a | **Required:** The license information for the app, if it falls under a different license than what Flow includes. |
| `repository` | (Repo URL) | The URL to the repository containing the source code if it is open-source. |
| `location` | "dynamic" | **Required:** The URL that points to your Flow installation, or 'dynamic' if it should be inferred from `window.location`. When building the desktop application, this is set to 'desktop'. |

> Note: Older versions of Flow Desktop up to v1.1.0-beta3 do not support setting location to a subdirectory (ex.: `https://example.com/app/`). Newer versions of Flow Desktop starting with v1.1.0-beta4 support subdirectory installations.

### License information

| Subkey | Default value | Description |
| --- | --- | ------------------------ |
| `name` | "Non-violent Public License" | The name of the license the app is under. |
| `url` | "https&#58;//thufie.lain.haus/NPL.html" | The URL to the license online. |

### Example

```json
{
    "version": "1.0.0beta5",
    "developer": true,
    "license": {
        "name": "Non-violent Public License",
        "url": "https://thufie.lain.haus/NPL.html"
    },
    "repository": "https://github.com/hyperspacedev/hyperspace",
    "location": "dynamic",
}
```

## An example of `config.json`

```json
{
    "version": "1.0.0beta1",
    "location": "dynamic",
    "branding": {
        "name": "Flow",
        "logo": "logo.svg",
        "background": "background.png"
    },
    "developer": true,
    "federated": {
        "universalLogin": true,
        "allowPublicPosts": true,
        "enablePublicTimeline": true
    },
    "registration": {
        "defaultInstance": "mastodon.social"
    },
    "admin": {
        "name": "Eugen",
        "account": "1"
    },
    "license": {
        "name": "Non-violent Public License",
        "url": "https://thufie.lain.haus/NPL.html"
    },
    "repository": "https://github.com/hyperspacedev/hyperspace"
}
```