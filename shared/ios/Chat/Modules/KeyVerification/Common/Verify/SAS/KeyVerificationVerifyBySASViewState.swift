// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Verify DeviceVerificationVerify

import Foundation

/// KeyVerificationVerifyBySASViewController view state
enum KeyVerificationVerifyViewState {
    case loading
    case loaded // verified
    case cancelled(MXTransactionCancelCode)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
