// File created from ScreenTemplate
// $ createScreen.sh Details SettingsDiscoveryThreePidDetails

import Foundation

/// SettingsDiscoveryThreePidDetailsViewController view actions exposed to view model
enum SettingsDiscoveryThreePidDetailsViewAction {
    case load
    case share
    case revoke
    case cancelThreePidValidation
    case confirmEmailValidation
    case confirmMSISDNValidation(code: String)
}
