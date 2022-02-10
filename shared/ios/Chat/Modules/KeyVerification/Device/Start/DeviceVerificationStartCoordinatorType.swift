// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Start DeviceVerificationStart

import Foundation

protocol DeviceVerificationStartCoordinatorDelegate: AnyObject {
    func deviceVerificationStartCoordinator(_ coordinator: DeviceVerificationStartCoordinatorType, didCompleteWithOutgoingTransaction transaction: MXSASTransaction)
    func deviceVerificationStartCoordinator(_ coordinator: DeviceVerificationStartCoordinatorType, didTransactionCancelled transaction: MXSASTransaction)

    func deviceVerificationStartCoordinatorDidCancel(_ coordinator: DeviceVerificationStartCoordinatorType)
}

/// `DeviceVerificationStartCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol DeviceVerificationStartCoordinatorType: Coordinator, Presentable {
    var delegate: DeviceVerificationStartCoordinatorDelegate? { get }
}
