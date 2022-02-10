import SwiftUI

@available(iOS 14.0, *)
struct PrimaryActionButtonStyle: ButtonStyle {
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    var enabled: Bool = false
    
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .padding(12.0)
            .frame(maxWidth: .infinity)
            .foregroundColor(.white)
            .font(theme.fonts.body)
            .background(configuration.isPressed ? theme.colors.accent.opacity(0.6) : theme.colors.accent)
            .opacity(enabled ? 1.0 : 0.6)
            .cornerRadius(8.0)
    }
}

@available(iOS 14.0, *)
struct PrimaryActionButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            VStack {
                Button("Enabled") { }
                    .buttonStyle(PrimaryActionButtonStyle(enabled: true))
                
                Button("Disabled") { }
                    .buttonStyle(PrimaryActionButtonStyle(enabled: false))
                    .disabled(true)
            }
            .padding()
        }
    }
}
