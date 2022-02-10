import Foundation
import Combine

/// Provides the theme and theme updates to SwiftUI.
///
/// Replaces the old ThemeObserver. Chat app can push updates to this class
/// removing the dependency of this class on the `ThemeService`.
@available(iOS 14.0, *)
class ThemePublisher: ObservableObject {
    
    private static var _shared: ThemePublisher? = nil
    static var shared: ThemePublisher {
            if _shared == nil {
                configure(themeId: .light)
            }
            return _shared!
    }
    
    @Published private(set) var theme: ThemeSwiftUI
    
    static func configure(themeId: ThemeIdentifier) {
        _shared = ThemePublisher(themeId: themeId)
    }
    
    init(themeId: ThemeIdentifier) {
        _theme = Published.init(initialValue: themeId.themeSwiftUI)
    }

    func republish(themeIdPublisher: AnyPublisher<ThemeIdentifier, Never>) {
        themeIdPublisher
            .map(\.themeSwiftUI)
            .assign(to: &$theme)
    }
}
