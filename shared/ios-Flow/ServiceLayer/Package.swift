// swift-tools-version:5.3

import PackageDescription

let package = Package(
    name: "ServiceLayer",
    platforms: [
        .iOS(.v14),
        .macOS(.v11)
    ],
    products: [
        .library(
            name: "ServiceLayer",
            targets: ["ServiceLayer"]),
        .library(
            name: "ServiceLayerMocks",
            targets: ["ServiceLayerMocks"])
    ],
    dependencies: [
        .package(path: "CombineExpectations"),
        .package(path: "CodableBloomFilter"),
        .package(path: "DB"),
        .package(path: "Keychain"),
        .package(path: "MastodonAPI"),
        .package(path: "Secrets")
    ],
    targets: [
        .target(
            name: "ServiceLayer",
            dependencies: ["CodableBloomFilter", "DB", "MastodonAPI", "Secrets"],
            resources: [.process("Resources")]),
        .target(
            name: "ServiceLayerMocks",
            dependencies: [
                "ServiceLayer",
                .product(name: "MastodonAPIStubs", package: "MastodonAPI"),
                .product(name: "MockKeychain", package: "Keychain")]),
        .testTarget(
            name: "ServiceLayerTests",
            dependencies: ["CombineExpectations", "ServiceLayerMocks"])
    ]
)
