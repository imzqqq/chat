import SwiftUI

@available(iOS 14.0, *)
struct TemplateRoomChatBubbleMessage: View {

    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    let messageContent: TemplateRoomChatMessageTextContent
    
    var body: some View {
        Text(messageContent.body)
            .accessibility(identifier: "bubbleTextContent")
            .foregroundColor(theme.colors.primaryContent)
            .font(theme.fonts.body)
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateRoomChatBubbleMessage_Previews: PreviewProvider {
    static let message = TemplateRoomChatMessageTextContent(body: "Hello")
    static var previews: some View {
        TemplateRoomChatBubbleMessage(messageContent: message)
    }
}
