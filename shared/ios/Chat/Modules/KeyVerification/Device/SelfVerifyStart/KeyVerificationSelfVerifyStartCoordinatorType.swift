// File created from ScreenTemplate
// $ createScreen.sh KeyVerification KeyVerificationSelfVerifyStart

import Foundation

protocol KeyVerificationSelfVerifyStartCoordinatorDelegate: AnyObject {
    
    func keyVerificationSelfVerifyStartCoordinator(_ coordinator: KeyVerificationSelfVerifyStartCoordinatorType, otherDidAcceptRequest request: MXKeyVerificationRequest)
    
    func keyVerificationSelfVerifyStartCoordinatorDidCancel(_ coordinator: KeyVerificationSelfVerifyStartCoordinatorType)
}

/// `KeyVerificationSelfVerifyStartCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol KeyVerificationSelfVerifyStartCoordinatorType: Coordinator, Presentable {
    var delegate: KeyVerificationSelfVerifyStartCoordinatorDelegate? { get }
}
