// File created from ScreenTemplate
// $ createScreen.sh KeyVerification/Device/ManuallyVerify KeyVerificationManuallyVerify

import Foundation

struct KeyVerificationManuallyVerifyViewData {
    let deviceId: String
    let deviceName: String?
    let deviceKey: String?
}

/// KeyVerificationManuallyVerifyViewController view state
enum KeyVerificationManuallyVerifyViewState {
    case loading
    case loaded(_ viewData: KeyVerificationManuallyVerifyViewData)
    case error(Error)
}
