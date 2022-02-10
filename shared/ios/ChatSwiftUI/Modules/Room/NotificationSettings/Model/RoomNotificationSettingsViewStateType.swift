import Foundation

protocol RoomNotificationSettingsViewStateType {
    var saving: Bool { get }
    var roomEncrypted: Bool { get }
    var notificationOptions: [RoomNotificationState] { get }
    var notificationState: RoomNotificationState { get }
    var avatarData: AvatarProtocol? { get }
    var displayName: String? { get }
}

