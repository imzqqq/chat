// File created from ScreenTemplate
// $ createScreen.sh Details SettingsDiscoveryThreePidDetails

import Foundation

/// SettingsDiscoveryThreePidDetailsViewController view state
enum SettingsDiscoveryThreePidDetailsViewState {
    case loading
    case loaded(displayMode: SettingsDiscoveryThreePidDetailsDisplayMode)
    case error(Error)
}

enum SettingsDiscoveryThreePidDetailsDisplayMode {
    case share
    case revoke
    case pendingThreePidVerification
}
