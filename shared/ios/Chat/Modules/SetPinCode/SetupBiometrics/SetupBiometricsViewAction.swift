// File created from ScreenTemplate
// $ createScreen.sh SetPinCode/SetupBiometrics SetupBiometrics

import Foundation

/// SetupBiometricsViewController view actions exposed to view model
enum SetupBiometricsViewAction {
    case loadData
    case enableDisableTapped
    case skipOrCancel
    case unlock
    case cantUnlockedAlertResetAction
}
