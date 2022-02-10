import Foundation

/// Represents a grouped bubble in the View(For example multiple message of different time sent close together).
struct TemplateRoomChatBubble {
    let id: String
    let sender: TemplateRoomChatMember
    var items: [TemplateRoomChatBubbleItem]
}

extension TemplateRoomChatBubble: Identifiable, Equatable { }
