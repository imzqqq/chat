import Foundation

/// SettingsDiscoveryTableViewSection view actions exposed to view model
enum SettingsDiscoveryViewAction {
    case load
    case acceptTerms
    case select(threePid: MX3PID)
}
