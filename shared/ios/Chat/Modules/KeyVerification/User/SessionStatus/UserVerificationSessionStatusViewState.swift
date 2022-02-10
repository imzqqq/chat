// File created from ScreenTemplate
// $ createScreen.sh SessionStatus UserVerificationSessionStatus

import Foundation

/// UserVerificationSessionStatusViewController view state
enum UserVerificationSessionStatusViewState {
    case loading
    case loaded(viewData: SessionStatusViewData)
    case error(Error)
}
