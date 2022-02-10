import Foundation
import SwiftUI

@available(iOS 14.0, *)
/// A modifier for showing the activity indicator centered over a view.
struct ActivityIndicatorModifier: ViewModifier {
    var show: Bool

    @ViewBuilder
    func body(content: Content) -> some View {
        if show {
            content
                .overlay(ActivityIndicator(), alignment: .center)
        } else {
            content
        }
    }
}

@available(iOS 14.0, *)
extension View {
    @available(iOS 14.0, *)
    func activityIndicator(show: Bool) -> some View {
        self.modifier(ActivityIndicatorModifier(show: show))
    }
}
