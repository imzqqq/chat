import Foundation
import SwiftUI

@available(iOS 14.0, *)
class StateRenderer {
    var states: [ScreenStateInfo]
    init(states: [ScreenStateInfo]) {
        self.states = states
    }
    
    /// Render each of the screen states in a group applying
    /// any optional environment variables.
    /// - Parameters:
    ///   - themeId: id of theme to render the screens with.
    ///   - locale: Locale to render the screens with.
    ///   - sizeCategory: type sizeCategory to render the screens with.
    ///   - addNavigation: Wether to wrap the screens in a navigation view.
    /// - Returns: The group of screens
    func screenGroup(
        addNavigation: Bool = false
    ) -> some View {
        Group {
            ForEach(0..<states.count) { i in
                let state = self.states[i]
                Self.wrapWithNavigation(addNavigation, view: state.view)
                    .previewDisplayName(state.stateTitle)
            }
        }
    }
    
    @ViewBuilder
    static func wrapWithNavigation<V: View>(_ wrap: Bool, view: V) -> some View {
        if wrap {
            NavigationView{
                view
                    .navigationBarTitleDisplayMode(.inline)
            }
        } else {
            view
        }
    }
}
