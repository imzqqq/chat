import Foundation
import UIKit

// Figma Avatar Sizes: https://www.figma.com/file/X4XTH9iS2KGJ2wFKDqkyed/Compound?node-id=1258%3A19678
public enum AvatarSize: Int {
    case xxSmall = 16
    case xSmall = 32
    case small = 36
    case medium = 42
    case large = 44
    case xLarge = 52
    case xxLarge = 80
}

extension AvatarSize {
    public var size: CGSize {
        return CGSize(width: self.rawValue, height: self.rawValue)
    }
}
