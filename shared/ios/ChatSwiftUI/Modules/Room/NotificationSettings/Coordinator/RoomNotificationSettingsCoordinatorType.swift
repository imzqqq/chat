import Foundation

protocol RoomNotificationSettingsCoordinatorDelegate: AnyObject {
    func roomNotificationSettingsCoordinatorDidComplete(_ coordinator: RoomNotificationSettingsCoordinatorType)
    func roomNotificationSettingsCoordinatorDidCancel(_ coordinator: RoomNotificationSettingsCoordinatorType)
}

/// `RoomNotificationSettingsCoordinatorType` is a protocol describing a Coordinator that handles changes to the room navigation settings navigation flow.
protocol RoomNotificationSettingsCoordinatorType: Coordinator, Presentable {
    var delegate: RoomNotificationSettingsCoordinatorDelegate? { get }
}
