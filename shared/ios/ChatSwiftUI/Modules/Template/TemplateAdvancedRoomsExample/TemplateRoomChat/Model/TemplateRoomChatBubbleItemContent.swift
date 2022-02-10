import Foundation

enum TemplateRoomChatBubbleItemContent {
    case message(TemplateRoomChatMessageContent)
}

extension TemplateRoomChatBubbleItemContent: Equatable { }
