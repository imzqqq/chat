import SwiftUI

@available(iOS 14.0, *)
struct UserSuggestionListItem: View {
    
    // MARK: - Properties
    
    // MARK: Private
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    let avatar: AvatarInputProtocol?
    let displayName: String?
    let userId: String
    
    var body: some View {
        HStack {
            if let avatar = avatar {
                AvatarImage(avatarData: avatar, size: .medium)
            }
            VStack(alignment: .leading) {
                Text(displayName ?? "")
                    .font(theme.fonts.body)
                    .foregroundColor(theme.colors.primaryContent)
                    .accessibility(identifier: "displayNameText")
                    .lineLimit(1)
                Text(userId)
                    .font(theme.fonts.footnote)
                    .foregroundColor(theme.colors.tertiaryContent)
                    .accessibility(identifier: "userIdText")
                    .lineLimit(1)
            }
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct UserSuggestionHeader_Previews: PreviewProvider {
    static var previews: some View {
        UserSuggestionListItem(avatar: MockAvatarInput.example, displayName: "Alice", userId: "@alice:chat.imzqqq.top")
            .addDependency(MockAvatarService.example)
    }
}
