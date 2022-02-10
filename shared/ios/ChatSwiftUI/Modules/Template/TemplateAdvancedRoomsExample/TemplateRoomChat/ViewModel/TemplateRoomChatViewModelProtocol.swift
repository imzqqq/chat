import Foundation

protocol TemplateRoomChatViewModelProtocol {
    var callback: ((TemplateRoomChatViewModelAction) -> Void)? { get set }
}
