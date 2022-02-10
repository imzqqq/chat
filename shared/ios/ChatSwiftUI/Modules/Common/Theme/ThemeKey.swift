import Foundation
import SwiftUI
import DesignKit

@available(iOS 14.0, *)
private struct ThemeKey: EnvironmentKey {
    static let defaultValue = ThemePublisher.shared.theme
}

@available(iOS 14.0, *)
extension EnvironmentValues {
  var theme: ThemeSwiftUI {
    get { self[ThemeKey.self] }
    set { self[ThemeKey.self] = newValue }
  }
}

@available(iOS 14.0, *)
extension View {
    /// A theme modifier for setting the theme for this view and all its descendants in the hierarchy.
    /// - Parameter theme: A theme to be set as the environment value.
    /// - Returns: The target view with the theme applied.
  func theme(_ theme: ThemeSwiftUI) -> some View {
    environment(\.theme, theme)
  }
}

@available(iOS 14.0, *)
extension View {
    /// A theme modifier for setting the theme by id for this view and all its descendants in the hierarchy.
    /// - Parameter themeId: ThemeIdentifier of a theme to be set as the environment value.
    /// - Returns: The target view with the theme applied.
  func theme(_ themeId: ThemeIdentifier) -> some View {
    return environment(\.theme, themeId.themeSwiftUI)
  }
}
