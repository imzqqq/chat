// File created from ScreenTemplate
// $ createScreen.sh Test SettingsIdentityServer

import Foundation

/// SettingsIdentityServerViewController view actions exposed to view model
enum SettingsIdentityServerViewAction {
    case load
    case add(identityServer: String)
    case change(identityServer: String)
    case disconnect
}
