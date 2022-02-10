// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Incoming DeviceVerificationIncoming

import Foundation

/// DeviceVerificationIncomingViewController view state
enum DeviceVerificationIncomingViewState {
    case loading
    case loaded // accepted
    case cancelled(MXTransactionCancelCode)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
