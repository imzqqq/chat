// File created from ScreenTemplate
// $ createScreen.sh KeyVerification/Common/ScanConfirmation KeyVerificationScanConfirmation

import Foundation

/// KeyVerificationScanConfirmationViewController view actions exposed to view model
enum KeyVerificationScanConfirmationViewAction {
    case loadData
    case acknowledgeOtherScannedMyCode(_ otherScannedMyCode: Bool)
    case cancel
}
