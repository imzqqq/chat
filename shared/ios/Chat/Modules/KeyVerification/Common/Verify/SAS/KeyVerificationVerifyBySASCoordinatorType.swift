// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Verify DeviceVerificationVerify

import Foundation

protocol KeyVerificationVerifyBySASCoordinatorDelegate: AnyObject {
    func keyVerificationVerifyBySASCoordinatorDidComplete(_ coordinator: KeyVerificationVerifyBySASCoordinatorType)
    func keyVerificationVerifyBySASCoordinatorDidCancel(_ coordinator: KeyVerificationVerifyBySASCoordinatorType)
}

/// `KeyVerificationVerifyBySASCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol KeyVerificationVerifyBySASCoordinatorType: Coordinator, Presentable {
    var delegate: KeyVerificationVerifyBySASCoordinatorDelegate? { get }
}
