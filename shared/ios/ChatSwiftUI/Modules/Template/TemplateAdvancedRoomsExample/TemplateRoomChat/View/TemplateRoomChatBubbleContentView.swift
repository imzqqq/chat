import SwiftUI

@available(iOS 14.0, *)
struct TemplateRoomChatBubbleContentView: View {
    
    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    let bubbleItem: TemplateRoomChatBubbleItem
    
    var body: some View {
        switch bubbleItem.content {
        case .message(let messageContent):
            switch messageContent {
            case .text(let messageContent):
                TemplateRoomChatBubbleMessage(messageContent: messageContent)
            case .image(let imageContent):
                TemplateRoomChatBubbleImage(imageContent: imageContent)
            }
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateRoomChatBubbleItemView_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}
