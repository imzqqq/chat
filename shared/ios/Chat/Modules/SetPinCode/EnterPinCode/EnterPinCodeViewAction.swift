// File created from ScreenTemplate
// $ createScreen.sh SetPinCode/EnterPinCode EnterPinCode

import Foundation

/// EnterPinCodeViewController view actions exposed to view model
enum EnterPinCodeViewAction {
    case loadData
    case digitPressed(_ tag: Int)
    case forgotPinPressed
    case cancel
    case pinsDontMatchAlertAction
    case forgotPinAlertResetAction
    case forgotPinAlertCancelAction
}
