import Foundation

protocol TemplateRoomListViewModelProtocol {
    var callback: ((TemplateRoomListViewModelAction) -> Void)? { get set }
    @available(iOS 14, *)
    var context: TemplateRoomListViewModelType.Context { get }
}
