import SwiftUI

/// A Modifier to be called from the top-most SwiftUI view before being added to a HostViewController.
///
/// Provides any app level configuration the SwiftUI hierarchy might need (E.g. to monitor theme changes).
@available(iOS 14.0, *)
struct VectorContentModifier: ViewModifier {
    
    @ObservedObject private var themePublisher = ThemePublisher.shared
    
    func body(content: Content) -> some View {
        content
            .theme(themePublisher.theme)
    }
}

@available(iOS 14.0, *)
extension View {
    func vectorContent() -> some View {
        self.modifier(VectorContentModifier())
    }
}
