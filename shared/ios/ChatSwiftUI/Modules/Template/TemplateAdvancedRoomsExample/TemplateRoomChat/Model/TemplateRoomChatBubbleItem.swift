import UIKit

/// One of the items grouped within a bubble(could be message types like text, image or video, or could be other items like url previews).
struct TemplateRoomChatBubbleItem {
    let id: String
    var timestamp: Date
    var content: TemplateRoomChatBubbleItemContent
}

extension TemplateRoomChatBubbleItem: Identifiable, Equatable { }


