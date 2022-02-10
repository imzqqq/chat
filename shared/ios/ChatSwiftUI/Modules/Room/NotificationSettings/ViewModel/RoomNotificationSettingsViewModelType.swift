import Foundation

protocol RoomNotificationSettingsViewModelViewDelegate: AnyObject {
    func roomNotificationSettingsViewModel(_ viewModel: RoomNotificationSettingsViewModelType, didUpdateViewState viewState: RoomNotificationSettingsViewStateType)
}

protocol RoomNotificationSettingsViewModelCoordinatorDelegate: AnyObject {
    func roomNotificationSettingsViewModelDidComplete(_ viewModel: RoomNotificationSettingsViewModelType)
    func roomNotificationSettingsViewModelDidCancel(_ viewModel: RoomNotificationSettingsViewModelType)
}

/// Protocol describing the view model used by `RoomNotificationSettingsViewController`
protocol RoomNotificationSettingsViewModelType {        
        
    var viewDelegate: RoomNotificationSettingsViewModelViewDelegate? { get set }
    var coordinatorDelegate: RoomNotificationSettingsViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: RoomNotificationSettingsViewAction)
}
