import Foundation

/// State managed by the `ViewModel` delivered to the `View`.
struct TemplateRoomChatViewState: BindableState {
    var roomInitializationStatus: TemplateRoomChatRoomInitializationStatus
    let roomName: String?
    var bubbles: [TemplateRoomChatBubble]
    var bindings: TemplateRoomChatViewModelBindings
}

extension TemplateRoomChatViewState {
    var sendButtonEnabled: Bool {
        !bindings.messageInput.isEmpty
    }
}

