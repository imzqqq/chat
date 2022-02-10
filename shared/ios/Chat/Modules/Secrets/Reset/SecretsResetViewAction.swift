// File created from ScreenTemplate
// $ createScreen.sh Secrets/Reset SecretsReset

import Foundation

/// SecretsResetViewController view actions exposed to view model
enum SecretsResetViewAction {
    case loadData
    case reset
    case authenticationInfoEntered(_ authInfo: [String: Any])
    case cancel
}
