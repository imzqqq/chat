import Foundation
import SwiftUI

/// An input field style for forms.
@available(iOS 14.0, *)
struct FormInputFieldStyle: TextFieldStyle {
    
    @Environment(\.theme) var theme: ThemeSwiftUI
    @Environment(\.isEnabled) var isEnabled
    
    private var textColor: Color {
        if !isEnabled {
            return theme.colors.quarterlyContent
        }
        return theme.colors.primaryContent
    }
    
    private var backgroundColor: Color {
        if !isEnabled && theme.identifier == .dark {
            return theme.colors.quinaryContent
        }
        return theme.colors.background
    }
    
    func _body(configuration: TextField<_Label>) -> some View {
        configuration
            .font(theme.fonts.callout)
            .foregroundColor(textColor)
            .frame(minHeight: 48)
            .padding(.horizontal)
            .background(backgroundColor)
    }
}


@available(iOS 14.0, *)
struct FormInputFieldStyle_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            VectorForm {
                TextField("Placeholder", text: .constant(""))
                    .textFieldStyle(FormInputFieldStyle())
                TextField("Placeholder", text: .constant("Web"))
                    .textFieldStyle(FormInputFieldStyle())
                TextField("Placeholder", text: .constant("Web"))
                    .textFieldStyle(FormInputFieldStyle())
                    .disabled(true)
                
            }
            .padding()
            VectorForm {
                TextField("Placeholder", text: .constant(""))
                    .textFieldStyle(FormInputFieldStyle())
                TextField("Placeholder", text: .constant("Web"))
                    .textFieldStyle(FormInputFieldStyle())
                TextField("Placeholder", text: .constant("Web"))
                    .textFieldStyle(FormInputFieldStyle())
                    .disabled(true)
                
            }
            .padding()
            .theme(ThemeIdentifier.dark)
        }

    }
}
