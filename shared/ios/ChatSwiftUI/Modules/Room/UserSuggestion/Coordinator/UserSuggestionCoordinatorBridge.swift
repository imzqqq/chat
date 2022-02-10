import Foundation

@objc
protocol UserSuggestionCoordinatorBridgeDelegate: AnyObject {
    func userSuggestionCoordinatorBridge(_ coordinator: UserSuggestionCoordinatorBridge, didRequestMentionForMember member: MXRoomMember, textTrigger: String?)
}

@objcMembers
final class UserSuggestionCoordinatorBridge: NSObject {
    
    private var _userSuggestionCoordinator: Any? = nil
    @available(iOS 14.0, *)
    fileprivate var userSuggestionCoordinator: UserSuggestionCoordinator {
        return _userSuggestionCoordinator as! UserSuggestionCoordinator
    }
    
    weak var delegate: UserSuggestionCoordinatorBridgeDelegate?
    
    init(mediaManager: MXMediaManager, room: MXRoom) {
        let parameters = UserSuggestionCoordinatorParameters(mediaManager: mediaManager, room: room)
        if #available(iOS 14.0, *) {
            let userSuggestionCoordinator = UserSuggestionCoordinator(parameters: parameters)
            self._userSuggestionCoordinator = userSuggestionCoordinator
        }
        
        super.init()
        
        if #available(iOS 14.0, *) {
            userSuggestionCoordinator.delegate = self
        }
    }
    
    func processTextMessage(_ textMessage: String) {
        if #available(iOS 14.0, *) {
            return self.userSuggestionCoordinator.processTextMessage(textMessage)
        }
    }
    
    func toPresentable() -> UIViewController? {
        if #available(iOS 14.0, *) {
            return self.userSuggestionCoordinator.toPresentable()
        }
        
        return nil
    }
}

@available(iOS 14.0, *)
extension UserSuggestionCoordinatorBridge: UserSuggestionCoordinatorDelegate {
    func userSuggestionCoordinator(_ coordinator: UserSuggestionCoordinator, didRequestMentionForMember member: MXRoomMember, textTrigger: String?) {
        delegate?.userSuggestionCoordinatorBridge(self, didRequestMentionForMember: member, textTrigger: textTrigger)
    }
}
