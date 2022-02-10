# Base16

An extension of Swift's `Data` type to work with hexadecimal (Base16) encoding.

- [Installation](#installation)
- [Usage](#usage)
- [License](#license)

## Installation

### Swift Package Manager

Add a dependency to `https://github.com/metabolist/base16.git`, either in Xcode or by adding it to the `dependencies` array in your `Package.swift`:

```swift
dependencies: [
    .package(name: "Base16", url: "https://github.com/metabolist/base16.git", .upToNextMajor(from: "1.0.0"))
],
```

### CocoaPods

Add the following to your `Podfile`:

```ruby
pod 'Base-16', '~> 1.0'
```

## Usage

Base16 adds functions to `Data` that mirror `Foundation`'s functions for working with Base64 encoded data:

```swift
import Base16

let data = Data([182, 239, 215, 173, 251, 168, 76, 252,
                 140, 7, 39, 163, 56, 255, 171, 35,
                 121, 205, 26, 252, 53, 166, 159, 67,
                 100, 70, 140, 79, 47, 26, 138, 209])

data.base16EncodedString() // returns "b6efd7adfba84cfc8c0727a338ffab2379cd1afc35a69f4364468c4f2f1a8ad1"
data.base16EncodedString(options: [.uppercase]) // returns "B6EFD7ADFBA84CFC8C0727A338FFAB2379CD1AFC35A69F4364468C4F2F1A8AD1"
data.base16EncodedData() // returns the same thing as `base16EncodedString()` as UTF-8 data
data.base16EncodedData(options: [.uppercase]) // returns the same thing as `base16EncodedString(options: [.uppercase])` as UTF-8 data

try Data(base16Encoded: "b6efd7adfba84cfc8c0727a338ffab2379cd1afc35a69f4364468c4f2f1a8ad1") // equivalent to `data` variable above
try Data(base16Encoded: "B6EFD7ADFBA84CFC8C0727A338FFAB2379CD1AFC35A69F4364468C4F2F1A8AD1") // equivalent to `data` variable above
// The above initializers also exist for Base16 encoded UTF-8 `Data`
```

A common use case for encoding data to a Base16 string is for representing the `deviceToken` received in `UIApplicationDelegate`'s `application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data)` for registering apps for push notifications.

## License

Base16 is released under the MIT license. [See LICENSE](https://github.com/metabolist/base16/blob/main/LICENSE) for details.
