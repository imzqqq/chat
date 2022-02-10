import Foundation

/// SourceImage represents a local or remote image.
enum SourceImage {
    case local(_ image: UIImage)
    case remote(_ url: URL)
}
