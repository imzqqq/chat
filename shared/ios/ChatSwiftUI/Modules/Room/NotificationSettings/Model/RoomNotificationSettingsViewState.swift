import Foundation

/// RoomNotificationSettingsViewController view state
struct RoomNotificationSettingsViewState: RoomNotificationSettingsViewStateType {
    let roomEncrypted: Bool
    var saving: Bool
    var notificationState: RoomNotificationState
    var avatarData: AvatarProtocol?
    var displayName: String?
}

extension RoomNotificationSettingsViewState {
    var notificationOptions: [RoomNotificationState] {
        if roomEncrypted {
            return [.all, .mute]
        } else {
            return RoomNotificationState.allCases
        }
    }
}

extension RoomNotificationSettingsViewState {
    var roomEncryptedString: String {
        roomEncrypted ? VectorL10n.roomNotifsSettingsEncryptedRoomNotice : ""
    }
}

