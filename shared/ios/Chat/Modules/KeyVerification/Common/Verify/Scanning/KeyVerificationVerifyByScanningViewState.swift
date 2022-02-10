// File created from ScreenTemplate
// $ createScreen.sh Verify KeyVerificationVerifyByScanning

import Foundation

struct KeyVerificationVerifyByScanningViewData {
    let verificationKind: KeyVerificationKind
    let qrCodeData: Data?
    let showScanAction: Bool
}

/// KeyVerificationVerifyByScanningViewController view state
enum KeyVerificationVerifyByScanningViewState {
    case loading
    case loaded(viewData: KeyVerificationVerifyByScanningViewData)
    case scannedCodeValidated(isValid: Bool)    
    case cancelled(cancelCode: MXTransactionCancelCode, verificationKind: KeyVerificationKind)
    case cancelledByMe(MXTransactionCancelCode)
    case error(Error)
}
