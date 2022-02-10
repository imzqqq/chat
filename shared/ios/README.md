# Chat for IOS

## Build Instructions

> ### If you have already everything installed, opening the project workspace in Xcode should be as easy as

```bash
xcodegen                  # Create the xcodeproj with all project source files
pod install               # Create the xcworkspace with all project dependencies
open Chat.xcworkspace     # Open Xcode
```

---

## Flow

A free, open-source iOS Flow client.

### Building

To build Flow:

- Select the top-level "Flow" item in Xcode and change the team in each target's "Signing & Capabilities" settings to your own

All dependencies are managed using [Swift Package Manager](https://swift.org/package-manager) and will automatically be installed by Xcode.

### Flow Push Notifications

Push notifications will not work in development builds of Flow unless you host your own instance of [flow-apns] and change the `pushSubscriptionEndpointURL` constants in [IdentityService.swift] to its URL.

### Architecture

- Flow uses the [Model–view–viewmodel (MVVM) architectural pattern](https://en.wikipedia.org/wiki/Model–view–viewmodel).
- View models are clients of a service layer that abstracts network and local database logic.
- Different levels of the architecture are in different local Swift Packages. `import DB` and `import MastodonAPI` should generally only be done within the `ServiceLayer` package, and `import ServiceLayer` only within the `ViewModels` package.

### Acknowledgements

Flow uses the following third-party libraries:

- [BlurHash](https://github.com/woltapp/blurhash)
- [CombineExpectations](https://github.com/groue/CombineExpectations)
- [GRDB](https://github.com/groue/GRDB.swift)
- [SDWebImage](https://github.com/SDWebImage/SDWebImage)
- [SQLCipher](https://github.com/sqlcipher/sqlcipher)
