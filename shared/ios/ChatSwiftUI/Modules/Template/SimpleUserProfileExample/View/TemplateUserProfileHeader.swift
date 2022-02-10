import SwiftUI

@available(iOS 14.0, *)
struct TemplateUserProfileHeader: View {
    
    // MARK: - Properties
    
    // MARK: Private
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    let avatar: AvatarInputProtocol?
    let displayName: String?
    let presence: TemplateUserProfilePresence
    
    var body: some View {
        VStack {
            if let avatar = avatar {
                AvatarImage(avatarData: avatar, size: .xxLarge)
                .padding(.vertical)
            }
            VStack(spacing: 8){
                Text(displayName ?? "")
                    .font(theme.fonts.title3)
                    .accessibility(identifier: "displayNameText")
                    .padding(.horizontal)
                    .lineLimit(1)
                TemplateUserProfilePresenceView(presence: presence)
            }
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateUserProfileHeader_Previews: PreviewProvider {
    static var previews: some View {
        TemplateUserProfileHeader(avatar: MockAvatarInput.example, displayName: "Alice", presence: .online)
            .addDependency(MockAvatarService.example)
    }
}
