import SwiftUI

@available(iOS 14.0, *)
struct TemplateRoomListRow: View {

    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    let avatar: AvatarInputProtocol
    let displayName: String?
    
    var body: some View {
        HStack{
            AvatarImage(avatarData: avatar, size: .medium)
            Text(displayName ?? "")
                .foregroundColor(theme.colors.primaryContent)
                .accessibility(identifier: "roomNameText")
            Spacer()
        }
        //add to a style
        .padding(.horizontal)
        .padding(.vertical, 8)
        .frame(maxWidth: .infinity)
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateRoomListRow_Previews: PreviewProvider {
    static var previews: some View {
        TemplateRoomListRow(avatar: MockAvatarInput.example, displayName: "Alice")
            .addDependency(MockAvatarService.example)
    }
}
