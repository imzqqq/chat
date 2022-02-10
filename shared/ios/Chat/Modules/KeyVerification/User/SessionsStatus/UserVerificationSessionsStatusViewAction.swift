// File created from ScreenTemplate
// $ createScreen.sh UserVerification UserVerificationSessionsStatus

import Foundation

/// UserVerificationSessionsStatusViewController view actions exposed to view model
enum UserVerificationSessionsStatusViewAction {
    case loadData
    case selectSession(deviceId: String)
    case close
}
