import Foundation
import SwiftUI

@available(iOS 14.0, *)
struct FormItemButtonStyle: ButtonStyle {
    @Environment(\.theme) var theme: ThemeSwiftUI
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .background(configuration.isPressed ? theme.colors.system : theme.colors.background)
            .foregroundColor(theme.colors.primaryContent)
            .font(theme.fonts.body)
    }
}
