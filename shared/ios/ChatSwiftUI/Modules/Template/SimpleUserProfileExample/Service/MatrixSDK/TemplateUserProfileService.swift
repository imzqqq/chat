import Foundation
import Combine

@available(iOS 14.0, *)
class TemplateUserProfileService: TemplateUserProfileServiceProtocol {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private var listenerReference: Any?
    
    // MARK: Public
    
    var userId: String {
        session.myUser.userId
    }
    
    var displayName: String? {
        session.myUser.displayname
    }
    
    var avatarUrl: String? {
        session.myUser.avatarUrl
    }
    
    private(set) var presenceSubject: CurrentValueSubject<TemplateUserProfilePresence, Never>
    
    // MARK: - Setup
    
    init(session: MXSession) {
        self.session = session
        self.presenceSubject = CurrentValueSubject(TemplateUserProfilePresence(mxPresence: session.myUser.presence))
        self.listenerReference = setupPresenceListener()
    }

    deinit {
        guard let reference = listenerReference else { return }
        session.myUser.removeListener(reference)
    }
    
    func setupPresenceListener() -> Any? {
        let reference = session.myUser.listen { [weak self] event in
            guard let self = self,
                  let event = event,
                  case .presence = MXEventType(identifier: event.eventId)
            else { return }
            self.presenceSubject.send(TemplateUserProfilePresence(mxPresence: self.session.myUser.presence))
        }
        if reference == nil {
            UILog.error("[TemplateUserProfileService] Did not recieve a lisenter reference.")
        }
        return reference
    }
}

fileprivate extension TemplateUserProfilePresence {
    
    init(mxPresence: MXPresence) {
        switch mxPresence {
        case MXPresenceOnline:
            self = .online
        case MXPresenceUnavailable:
            self = .idle
        case MXPresenceOffline, MXPresenceUnknown:
            self = .offline
        default:
            self = .offline
        }
    }
}
