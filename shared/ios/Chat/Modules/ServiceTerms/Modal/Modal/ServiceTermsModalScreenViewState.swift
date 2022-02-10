// File created from ScreenTemplate
// $ createScreen.sh Modal/Show ServiceTermsModalScreen

import Foundation

/// ServiceTermsModalScreenViewController view state
enum ServiceTermsModalScreenViewState {
    case loading
    case loaded(policies: [MXLoginPolicyData], alreadyAcceptedPoliciesUrls: [String])
    case accepted
    case error(Error)
}
