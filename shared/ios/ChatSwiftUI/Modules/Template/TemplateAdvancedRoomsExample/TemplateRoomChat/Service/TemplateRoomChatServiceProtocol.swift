import Foundation
import Combine

@available(iOS 14.0, *)
protocol TemplateRoomChatServiceProtocol {
    var roomInitializationStatus: CurrentValueSubject<TemplateRoomChatRoomInitializationStatus, Never> { get }
    var chatMessagesSubject: CurrentValueSubject<[TemplateRoomChatMessage], Never> { get }
    var roomName: String? { get }
    func send(textMessage: String)
}
