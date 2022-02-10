import Foundation

/// Actions to be performed on the `ViewModel` State
enum TemplateRoomChatStateAction {
    case updateRoomInitializationStatus(TemplateRoomChatRoomInitializationStatus)
    case updateBubbles([TemplateRoomChatBubble])
    case clearMessageInput
}
