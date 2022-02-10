import SwiftUI

@available(iOS 14.0, *)
struct TemplateUserProfilePresenceView: View {
    
    // MARK: - Properties
    
    // MARK: Public
    let presence: TemplateUserProfilePresence
    
    var body: some View {
        HStack {
            Image(systemName: "circle.fill")
                .resizable()
                .frame(width: 8, height: 8)
            Text(presence.title)
                .font(.subheadline)
                .accessibilityIdentifier("presenceText")
        }
        .foregroundColor(foregroundColor)
        .padding(0)
    }
    
    // MARK: View Components
    
    private var foregroundColor: Color {
        switch presence {
        case .online:
            return .green
        case .idle:
            return .orange
        case .offline:
            return .gray
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateUserProfilePresenceView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(alignment:.leading){
            Text("Presence")
            ForEach(TemplateUserProfilePresence.allCases) { presence in
                TemplateUserProfilePresenceView(presence: presence)
            }
        }
    }
}
