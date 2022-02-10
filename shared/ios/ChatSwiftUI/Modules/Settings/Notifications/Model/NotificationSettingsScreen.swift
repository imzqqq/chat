import Foundation

/// The notification settings screen definitions, used when calling the coordinator.
@objc enum NotificationSettingsScreen: Int {
    case defaultNotifications
    case mentionsAndKeywords
    case other
}

extension NotificationSettingsScreen: CaseIterable { }

extension NotificationSettingsScreen: Identifiable {
    var id: Int { self.rawValue }
}

extension NotificationSettingsScreen {
    /// Defines which rules are handled by each of the screens.
    var pushRules: [NotificationPushRuleId] {
        switch self {
        case .defaultNotifications:
            return [.oneToOneRoom, .allOtherMessages, .oneToOneEncryptedRoom, .encrypted]
        case .mentionsAndKeywords:
            return [.containDisplayName, .containUserName, .roomNotif, .keywords]
        case .other:
            return [.inviteMe, .call, .suppressBots, .tombstone]
        }
    }
}
