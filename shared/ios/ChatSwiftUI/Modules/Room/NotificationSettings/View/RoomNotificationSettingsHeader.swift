import SwiftUI

@available(iOS 14.0, *)
struct RoomNotificationSettingsHeader: View {
    
    @Environment(\.theme) var theme: ThemeSwiftUI
    var avatarData: AvatarInputProtocol
    var displayName: String?
    
    var body: some View {
        HStack {
            Spacer()
            VStack(alignment: .center) {
                AvatarImage(avatarData: avatarData, size: .xxLarge)
                if let displayName = displayName {
                    Text(displayName)
                        .font(theme.fonts.title3SB)
                        .foregroundColor(theme.colors.primaryContent)
                        .textCase(nil)
                }
            }
            Spacer()
        }
        .padding(.top, 36)
    }
}

@available(iOS 14.0, *)
struct RoomNotificationSettingsHeader_Previews: PreviewProvider {
    static let name = "Element"
    static var previews: some View {
        RoomNotificationSettingsHeader(avatarData: MockAvatarInput.example, displayName: name)
            .addDependency(MockAvatarService.example)
    }
}
