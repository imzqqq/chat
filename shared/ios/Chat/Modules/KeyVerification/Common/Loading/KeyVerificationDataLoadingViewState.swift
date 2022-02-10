// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Loading DeviceVerificationDataLoading

import Foundation

/// KeyVerificationDataLoadingViewController view state
enum KeyVerificationDataLoadingViewState {
    case loading
    case loaded
    case error(Error)
    case errorMessage(String)
}
