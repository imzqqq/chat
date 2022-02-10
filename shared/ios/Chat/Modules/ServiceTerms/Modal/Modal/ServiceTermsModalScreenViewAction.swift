// File created from ScreenTemplate
// $ createScreen.sh Modal/Show ServiceTermsModalScreen

import Foundation

/// ServiceTermsModalScreenViewController view actions exposed to view model
enum ServiceTermsModalScreenViewAction {
    case load
    case display(MXLoginPolicyData)
    case accept
    case decline
}
