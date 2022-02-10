import Foundation

enum RoomNotificationState: Int {
    case all
    case mentionsAndKeywordsOnly
    case mute
}

extension RoomNotificationState: CaseIterable { }

extension RoomNotificationState: Identifiable {
    var id: Int { self.rawValue }
}

extension RoomNotificationState {
    var title: String {
        switch self {
        case .all:
            return VectorL10n.roomNotifsSettingsAllMessages
        case .mentionsAndKeywordsOnly:
            return VectorL10n.roomNotifsSettingsMentionsAndKeywords
        case .mute:
            return VectorL10n.roomNotifsSettingsNone
        }
    }
}
