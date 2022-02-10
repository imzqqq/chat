import Foundation

protocol NotificationSettingsViewModelCoordinatorDelegate: AnyObject {
    func notificationSettingsViewModelDidComplete(_ viewModel: NotificationSettingsViewModelType)
}

/// Protocol describing the view model used by `NotificationSettingsViewController`
protocol NotificationSettingsViewModelType {
    var coordinatorDelegate: NotificationSettingsViewModelCoordinatorDelegate? { get set }
}
