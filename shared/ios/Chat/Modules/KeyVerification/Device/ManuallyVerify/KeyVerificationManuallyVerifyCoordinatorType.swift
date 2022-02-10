// File created from ScreenTemplate
// $ createScreen.sh KeyVerification/Device/ManuallyVerify KeyVerificationManuallyVerify

import Foundation

protocol KeyVerificationManuallyVerifyCoordinatorDelegate: AnyObject {
    func keyVerificationManuallyVerifyCoordinator(_ coordinator: KeyVerificationManuallyVerifyCoordinatorType, didVerifiedDeviceWithId deviceId: String, of userId: String)    
    func keyVerificationManuallyVerifyCoordinatorDidCancel(_ coordinator: KeyVerificationManuallyVerifyCoordinatorType)
}

/// `KeyVerificationManuallyVerifyCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol KeyVerificationManuallyVerifyCoordinatorType: Coordinator, Presentable {
    var delegate: KeyVerificationManuallyVerifyCoordinatorDelegate? { get }
}
