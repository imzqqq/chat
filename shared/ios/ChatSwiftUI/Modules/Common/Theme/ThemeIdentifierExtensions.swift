import Foundation
import DesignKit

@available(iOS 14.0, *)
extension ThemeIdentifier {
    fileprivate static let defaultTheme = DefaultThemeSwiftUI()
    fileprivate static let darkTheme = DarkThemeSwiftUI()
    /// Extension to `ThemeIdentifier` for getting the SwiftUI theme.
    public var themeSwiftUI: ThemeSwiftUI {
        switch self {
        case .light:
            return Self.defaultTheme
        case .dark, .black:
            return Self.darkTheme
        }
    }
}
