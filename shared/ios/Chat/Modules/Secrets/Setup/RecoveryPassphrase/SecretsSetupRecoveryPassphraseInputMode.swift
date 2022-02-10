// File created from ScreenTemplate
// $ createScreen.sh Test SecretsSetupRecoveryPassphrase

import Foundation

enum SecretsSetupRecoveryPassphraseInput {
    case new
    case confirm(_ passphrase: String)
}
