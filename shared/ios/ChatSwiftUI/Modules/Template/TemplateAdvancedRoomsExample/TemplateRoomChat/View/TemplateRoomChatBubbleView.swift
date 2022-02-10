import SwiftUI

@available(iOS 14.0, *)
struct TemplateRoomChatBubbleView: View {

    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    let bubble: TemplateRoomChatBubble
    
    var body: some View {
        HStack(alignment: .top){
            AvatarImage(avatarData: bubble.sender.avatarData, size: .xSmall)
                .accessibility(identifier: "bubbleImage")
            VStack(alignment: .leading){
                Text(bubble.sender.displayName ?? "")
                    .foregroundColor(theme.displayNameColor(for: bubble.sender.id))
                    .font(theme.fonts.bodySB)
                ForEach(bubble.items) { item in
                    TemplateRoomChatBubbleContentView(bubbleItem: item)
                }
            }
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
struct TemplateRoomChatBubbleView_Previews: PreviewProvider {
    static let bubble = TemplateRoomChatBubble(
        id: "111",
        sender: MockTemplateRoomChatService.mockMessages[0].sender,
        items: [
            TemplateRoomChatBubbleItem(
                id: "222",
                timestamp: Date(),
                content: .message(.text(TemplateRoomChatMessageTextContent(body: "Hello")))
            )
        ]
    )
    static var previews: some View {
        TemplateRoomChatBubbleView(bubble: bubble)
            .addDependency(MockAvatarService.example)
    }
}
