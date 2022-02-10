// File created from ScreenTemplate
// $ createScreen.sh UserVerification UserVerificationSessionsStatus

import Foundation

/// UserVerificationSessionsStatusViewController view state
enum UserVerificationSessionsStatusViewState {
    case loading
    case loaded(userTrustLevel: UserEncryptionTrustLevel, sessionsStatusViewData: [UserVerificationSessionStatusViewData])
    case error(Error)
}
