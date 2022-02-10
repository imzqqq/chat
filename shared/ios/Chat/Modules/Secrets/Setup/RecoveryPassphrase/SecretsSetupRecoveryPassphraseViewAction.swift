// File created from ScreenTemplate
// $ createScreen.sh Test SecretsSetupRecoveryPassphrase

import Foundation

/// SecretsSetupRecoveryPassphraseViewController view actions exposed to view model
enum SecretsSetupRecoveryPassphraseViewAction {
    case loadData
    case updatePassphrase(_ passphrase: String?)
    case validate
    case cancel
}
