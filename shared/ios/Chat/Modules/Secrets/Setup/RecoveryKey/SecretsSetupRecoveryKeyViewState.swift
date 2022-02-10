// File created from ScreenTemplate
// $ createScreen.sh SecretsSetupRecoveryKey SecretsSetupRecoveryKey

import Foundation

/// SecretsSetupRecoveryKeyViewController view state
enum SecretsSetupRecoveryKeyViewState {
    case loaded(_ passphraseOnly: Bool)
    case loading
    case recoveryCreated(_ recoveryKey: String)
    case error(Error)
}
