import Foundation
/// RoomNotificationSettingsViewController view actions exposed to view model
enum RoomNotificationSettingsViewAction {
    case load
    case selectNotificationState(RoomNotificationState)
    case save
    case cancel
}
