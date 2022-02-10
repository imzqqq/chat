// File created from ScreenTemplate
// $ createScreen.sh SetPinCode/SetupBiometrics SetupBiometrics

import Foundation

/// SetupBiometricsViewController view state
enum SetupBiometricsViewState {
    case setupAfterLogin
    case setupFromSettings
    case unlock
    case confirmToDisable
    case cantUnlocked
}
