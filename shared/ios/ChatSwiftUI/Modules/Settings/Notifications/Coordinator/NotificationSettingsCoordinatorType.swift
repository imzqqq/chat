import Foundation

protocol NotificationSettingsCoordinatorDelegate: AnyObject {
    func notificationSettingsCoordinatorDidComplete(_ coordinator: NotificationSettingsCoordinatorType)
}

/// `NotificationSettingsCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol NotificationSettingsCoordinatorType: Coordinator, Presentable {
    var delegate: NotificationSettingsCoordinatorDelegate? { get }
}
