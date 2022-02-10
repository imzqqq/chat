// File created from ScreenTemplate
// $ createScreen.sh Start UserVerificationStart

import Foundation

/// UserVerificationStartViewController view state
enum UserVerificationStartViewState {
    case loading
    case loaded(UserVerificationStartViewData)
    case verificationPending
    case cancelled(MXTransactionCancelCode)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
