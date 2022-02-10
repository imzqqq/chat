// File created from ScreenTemplate
// $ createScreen.sh KeyVerification/Common/ScanConfirmation KeyVerificationScanConfirmation

import Foundation

struct KeyVerificationScanConfirmationViewData {
    let isScanning: Bool
    let verificationKind: KeyVerificationKind
    let otherDisplayName: String
}

/// KeyVerificationScanConfirmationViewController view state
enum KeyVerificationScanConfirmationViewState {
    case loading
    case loaded(_ viewData: KeyVerificationScanConfirmationViewData)    
    case cancelled(MXTransactionCancelCode)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
