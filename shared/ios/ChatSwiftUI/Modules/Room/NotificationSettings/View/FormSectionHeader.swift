import SwiftUI

@available(iOS 14.0, *)
struct FormSectionHeader: View {
    
    @Environment(\.theme) var theme: ThemeSwiftUI
    var text: String
    
    var body: some View {
        Text(text)
            .foregroundColor(theme.colors.secondaryContent)
            .padding(.top, 32)
            .padding(.leading)
            .padding(.bottom, 8)
            .font(theme.fonts.footnote)
            .textCase(.uppercase)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}

@available(iOS 14.0, *)
struct FormSectionHeader_Previews: PreviewProvider {
    static var previews: some View {
        VectorForm {
            SwiftUI.Section(header: FormSectionHeader(text: "Section Header")) {
                FormPickerItem(title: "Item 1", selected: false)
                FormPickerItem(title: "Item 2", selected: false)
                FormPickerItem(title: "Item 3", selected: false)
            }
        }
    }
}
