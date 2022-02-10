import Foundation
import Combine

@available(iOS 14.0, *)
class MockTemplateRoomChatService: TemplateRoomChatServiceProtocol {
    
    let roomName: String? = "New Vector"
    
    static let amadine = TemplateRoomChatMember(id: "@amadine:chat.dingshunyu.top", avatarUrl: "!aaabaa:chat.dingshunyu.top", displayName: "Amadine")
    static let mathew = TemplateRoomChatMember(id: "@mathew:chat.dingshunyu.top", avatarUrl: "!bbabb:chat.dingshunyu.top", displayName: "Mathew")
    static let mockMessages = [
        TemplateRoomChatMessage(id: "!0:chat.dingshunyu.top", content: .text(TemplateRoomChatMessageTextContent(body: "Shall I put it live?")) , sender: amadine, timestamp: Date(timeIntervalSinceNow: 60 * -3)),
        TemplateRoomChatMessage(id: "!1:chat.dingshunyu.top", content: .text(TemplateRoomChatMessageTextContent(body: "Yea go for it! ...and then let's head to the pub")), sender: mathew, timestamp: Date(timeIntervalSinceNow: 60)),
        TemplateRoomChatMessage(id: "!2:chat.dingshunyu.top", content: .text(TemplateRoomChatMessageTextContent(body: "Deal.")), sender: amadine, timestamp: Date(timeIntervalSinceNow: 60 * -2)),
        TemplateRoomChatMessage(id: "!3:chat.dingshunyu.top", content: .text(TemplateRoomChatMessageTextContent(body: "Ok, Done. 🍻")), sender: amadine, timestamp: Date(timeIntervalSinceNow: 60 * -1)),
    ]
    var roomInitializationStatus: CurrentValueSubject<TemplateRoomChatRoomInitializationStatus, Never>
    var chatMessagesSubject: CurrentValueSubject<[TemplateRoomChatMessage], Never>

    init(messages: [TemplateRoomChatMessage] = mockMessages) {
        self.roomInitializationStatus = CurrentValueSubject(.notInitialized)
        self.chatMessagesSubject = CurrentValueSubject(messages)
    }
    
    func send(textMessage: String) {
        let newMessage = TemplateRoomChatMessage(id: "!\(chatMessagesSubject.value.count):chat.dingshunyu.top", content: .text(TemplateRoomChatMessageTextContent(body: textMessage)), sender: Self.amadine, timestamp: Date())
        self.chatMessagesSubject.value += [newMessage]
    }
    
    func simulateUpdate(initializationStatus: TemplateRoomChatRoomInitializationStatus) {
        self.roomInitializationStatus.value = initializationStatus
    }
    
    func simulateUpdate(messages: [TemplateRoomChatMessage]) {
        self.chatMessagesSubject.value = messages
    }
}
