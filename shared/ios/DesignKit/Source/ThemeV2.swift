import Foundation
import UIKit

/// Theme v2. May be named again as `Theme` when the migration completed.
@objc public protocol ThemeV2 {
    
    /// Colors object
    var colors: ColorsUIKit { get }
    
    /// Fonts object
    var fonts: FontsUIKit { get }
    
    /// may contain more design components in future, like icons, audio files etc.
}

/// Theme v2 for SwiftUI.
@available(iOS 14.0, *)
public protocol ThemeSwiftUIType {
    
    /// Colors object
    var colors: ColorSwiftUI { get }
    
    /// Fonts object
    var fonts: FontSwiftUI { get }
    
    /// may contain more design components in future, like icons, audio files etc.
}
