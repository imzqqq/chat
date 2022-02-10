// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Incoming DeviceVerificationIncoming

import Foundation

protocol DeviceVerificationIncomingCoordinatorDelegate: AnyObject {
    func deviceVerificationIncomingCoordinator(_ coordinator: DeviceVerificationIncomingCoordinatorType, didAcceptTransaction message: MXSASTransaction)
    func deviceVerificationIncomingCoordinatorDidCancel(_ coordinator: DeviceVerificationIncomingCoordinatorType)
}

/// `DeviceVerificationIncomingCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol DeviceVerificationIncomingCoordinatorType: Coordinator, Presentable {
    var delegate: DeviceVerificationIncomingCoordinatorDelegate? { get }
}
