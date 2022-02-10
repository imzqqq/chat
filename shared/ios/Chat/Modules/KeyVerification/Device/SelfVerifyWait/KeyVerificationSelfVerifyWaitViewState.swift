// File created from ScreenTemplate
// $ createScreen.sh KeyVerification KeyVerificationSelfVerifyWait

import Foundation

enum SecretsRecoveryAvailability {
    case notAvailable
    case available(_ mode: SecretsRecoveryMode)
}

struct KeyVerificationSelfVerifyWaitViewData {
    let isNewSignIn: Bool
    let secretsRecoveryAvailability: SecretsRecoveryAvailability
}

/// KeyVerificationSelfVerifyWaitViewController view state
enum KeyVerificationSelfVerifyWaitViewState {
    case loading
    case secretsRecoveryCheckingAvailability(_ text: String?)
    case loaded(_ viewData: KeyVerificationSelfVerifyWaitViewData)
    case cancelled(MXTransactionCancelCode)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
