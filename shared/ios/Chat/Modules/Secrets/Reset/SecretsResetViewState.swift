// File created from ScreenTemplate
// $ createScreen.sh Secrets/Reset SecretsReset

import Foundation

/// SecretsResetViewController view state
enum SecretsResetViewState {
    case resetting
    case resetDone
    case error(Error)
}
