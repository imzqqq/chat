// File created from ScreenTemplate
// $ createScreen.sh SessionStatus UserVerificationSessionStatus

import Foundation

/// UserVerificationSessionStatusViewController view actions exposed to view model
enum UserVerificationSessionStatusViewAction {
    case loadData
    case verify
    case verifyManually
    case close
}
