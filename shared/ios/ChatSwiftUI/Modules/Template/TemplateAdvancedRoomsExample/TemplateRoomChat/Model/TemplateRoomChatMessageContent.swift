import Foundation

/// The type of message a long with it's content.
enum TemplateRoomChatMessageContent {
    case text(TemplateRoomChatMessageTextContent)
    case image(TemplateRoomChatMessageImageContent)
}

extension TemplateRoomChatMessageContent: Equatable { }
