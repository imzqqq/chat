// swift-tools-version:5.3

import PackageDescription

let package = Package(
    name: "DB",
    platforms: [
        .iOS(.v14),
        .macOS(.v11)
    ],
    products: [
        .library(
            name: "DB",
            targets: ["DB"])
    ],
    dependencies: [
        .package(path: "GRDB"),
        .package(path: "Mastodon"),
        .package(path: "Secrets")
    ],
    targets: [
        .target(
            name: "DB",
            dependencies: ["GRDB", "Mastodon", "Secrets"]),
        .testTarget(
            name: "DBTests",
            dependencies: ["DB"])
    ]
)
