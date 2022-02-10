// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Start DeviceVerificationStart

import Foundation

/// DeviceVerificationStartViewController view actions exposed to view model
enum DeviceVerificationStartViewAction {
    case beginVerifying
    case verifyUsingLegacy
    case verifiedUsingLegacy
    case cancel
}
