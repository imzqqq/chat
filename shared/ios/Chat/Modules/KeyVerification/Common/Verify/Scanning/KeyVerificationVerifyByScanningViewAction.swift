// File created from ScreenTemplate
// $ createScreen.sh Verify KeyVerificationVerifyByScanning

import Foundation

/// KeyVerificationVerifyByScanningViewController view actions exposed to view model
enum KeyVerificationVerifyByScanningViewAction {
    case loadData
    case cancel
    case scannedCode(payloadData: Data)
    case cannotScan    
    case acknowledgeMyUserScannedOtherCode
}
