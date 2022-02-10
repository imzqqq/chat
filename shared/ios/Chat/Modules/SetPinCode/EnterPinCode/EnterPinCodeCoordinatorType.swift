// File created from ScreenTemplate
// $ createScreen.sh SetPinCode/EnterPinCode EnterPinCode

import Foundation

protocol EnterPinCodeCoordinatorDelegate: AnyObject {
    func enterPinCodeCoordinatorDidComplete(_ coordinator: EnterPinCodeCoordinatorType)
    func enterPinCodeCoordinatorDidCompleteWithReset(_ coordinator: EnterPinCodeCoordinatorType, dueToTooManyErrors: Bool)
    func enterPinCodeCoordinator(_ coordinator: EnterPinCodeCoordinatorType, didCompleteWithPin pin: String)
    func enterPinCodeCoordinatorDidCancel(_ coordinator: EnterPinCodeCoordinatorType)
}

/// `EnterPinCodeCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol EnterPinCodeCoordinatorType: Coordinator, Presentable {
    var delegate: EnterPinCodeCoordinatorDelegate? { get }
}
