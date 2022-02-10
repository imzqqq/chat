import Foundation

/// A chat message send to the timeline within a room.
struct TemplateRoomChatMessage {
    let id: String
    let content: TemplateRoomChatMessageContent
    let sender: TemplateRoomChatMember
    let timestamp: Date
}

extension TemplateRoomChatMessage: Identifiable {}
