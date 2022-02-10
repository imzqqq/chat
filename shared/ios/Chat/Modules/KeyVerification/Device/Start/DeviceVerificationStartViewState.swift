// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Start DeviceVerificationStart

import Foundation

/// DeviceVerificationStartViewController view state
enum DeviceVerificationStartViewState {
    case loading
    case loaded     // started
    case verifyUsingLegacy(MXSession, MXDeviceInfo)
    case cancelled(MXTransactionCancelCode)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
