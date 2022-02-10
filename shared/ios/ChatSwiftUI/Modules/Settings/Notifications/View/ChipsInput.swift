import SwiftUI

/// Renders an input field and a collection of chips.
@available(iOS 14.0, *)
struct ChipsInput: View {
    
    @Environment(\.theme) var theme: ThemeSwiftUI
    @Environment(\.isEnabled) var isEnabled
    
    @State private var chipText: String = ""
    
    let titles: [String]
    let didAddChip: (String) -> Void
    let didDeleteChip: (String) -> Void
    var placeholder: String = ""
    
    
    var body: some View {
        VStack(spacing: 16) {
            TextField(placeholder, text: $chipText, onCommit: {
                didAddChip(chipText)
                chipText = ""
            })
            .disabled(!isEnabled)
            .disableAutocorrection(true)
            .autocapitalization(.none)
            .textFieldStyle(FormInputFieldStyle())
            Chips(titles: titles, didDeleteChip: didDeleteChip)
                .padding(.horizontal)
        }
    }
}

@available(iOS 14.0, *)
struct ChipsInput_Previews: PreviewProvider {
    static var chips = Set<String>(["Website", "Element", "Design", "Matrix/Element"])
    static var previews: some View {
        ChipsInput(titles: Array(chips)) { chip in
            chips.insert(chip)
        } didDeleteChip: { chip in
            chips.remove(chip)
        }
        .disabled(true)

    }
}
