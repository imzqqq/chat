import Foundation

enum TemplateUserProfilePresence {
    case online
    case idle
    case offline
}

extension TemplateUserProfilePresence {
    var title: String {
        switch self {
        case .online:
            return VectorL10n.roomParticipantsOnline
        case .idle:
            return VectorL10n.roomParticipantsIdle
        case .offline:
            return VectorL10n.roomParticipantsOffline
        }
    }
}

extension TemplateUserProfilePresence: CaseIterable { }

extension TemplateUserProfilePresence: Identifiable {
    var id: Self { self }
}
