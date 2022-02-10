// File created from ScreenTemplate
// $ createScreen.sh Test SecretsSetupRecoveryPassphrase

import Foundation

enum SecretsSetupRecoveryPassphraseViewDataMode {
    case newPassphrase(strength: PasswordStrength)
    case confimPassphrase
}

struct SecretsSetupRecoveryPassphraseViewData {
    let mode: SecretsSetupRecoveryPassphraseViewDataMode
    let isFormValid: Bool
}

/// SecretsSetupRecoveryPassphraseViewController view state
enum SecretsSetupRecoveryPassphraseViewState {
    case loaded(_ viewData: SecretsSetupRecoveryPassphraseViewData)
    case formUpdated(_ viewData: SecretsSetupRecoveryPassphraseViewData)
    case error(Error)
}
