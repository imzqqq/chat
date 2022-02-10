import Foundation
import Combine

@available(iOS 14.0, *)
class MockTemplateUserProfileService: TemplateUserProfileServiceProtocol {
    var presenceSubject: CurrentValueSubject<TemplateUserProfilePresence, Never>
    
    let userId: String
    let displayName: String?
    let avatarUrl: String?
    init(
        userId: String = "@alice:chat.dingshunyu.top",
        displayName:  String? = "Alice",
        avatarUrl: String? = "mxc://chat.dingshunyu.top/VyNYAgahaiAzUoOeZETtQ",
        presence: TemplateUserProfilePresence = .offline
    ) {
        self.userId = userId
        self.displayName = displayName
        self.avatarUrl = avatarUrl
        self.presenceSubject = CurrentValueSubject<TemplateUserProfilePresence, Never>(presence)
    }
    
    func simulateUpdate(presence: TemplateUserProfilePresence) {
        self.presenceSubject.value = presence
    }
}
