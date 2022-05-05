// swift-tools-version:5.3

import PackageDescription

let package = Package(
    name: "Base16",
    products: [
        .library(
            name: "Base16",
            targets: ["Base16"]),
    ],
    dependencies: [],
    targets: [
        .target(
            name: "Base16",
            dependencies: []),
        .testTarget(
            name: "Base16Tests",
            dependencies: ["Base16"]),
    ]
)
