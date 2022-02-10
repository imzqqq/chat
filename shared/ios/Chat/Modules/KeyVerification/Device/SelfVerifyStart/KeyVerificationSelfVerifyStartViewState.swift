// File created from ScreenTemplate
// $ createScreen.sh KeyVerification KeyVerificationSelfVerifyStart

import Foundation

/// KeyVerificationSelfVerifyStartViewController view state
enum KeyVerificationSelfVerifyStartViewState {
    case loading
    case loaded
    case verificationPending
    case cancelled(MXTransactionCancelCode)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
