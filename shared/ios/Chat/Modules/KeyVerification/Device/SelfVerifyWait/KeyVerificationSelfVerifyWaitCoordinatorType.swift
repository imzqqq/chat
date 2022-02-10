// File created from ScreenTemplate
// $ createScreen.sh KeyVerification KeyVerificationSelfVerifyWait

import Foundation

protocol KeyVerificationSelfVerifyWaitCoordinatorDelegate: AnyObject {
    func keyVerificationSelfVerifyWaitCoordinator(_ coordinator: KeyVerificationSelfVerifyWaitCoordinatorType, didAcceptKeyVerificationRequest keyVerificationRequest: MXKeyVerificationRequest)
    func keyVerificationSelfVerifyWaitCoordinator(_ coordinator: KeyVerificationSelfVerifyWaitCoordinatorType, didAcceptIncomingSASTransaction incomingSASTransaction: MXIncomingSASTransaction)
    func keyVerificationSelfVerifyWaitCoordinatorDidCancel(_ coordinator: KeyVerificationSelfVerifyWaitCoordinatorType)
    func keyVerificationSelfVerifyWaitCoordinator(_ coordinator: KeyVerificationSelfVerifyWaitCoordinatorType, wantsToRecoverSecretsWith secretsRecoveryMode: SecretsRecoveryMode)
}

/// `KeyVerificationSelfVerifyWaitCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol KeyVerificationSelfVerifyWaitCoordinatorType: Coordinator, Presentable {
    var delegate: KeyVerificationSelfVerifyWaitCoordinatorDelegate? { get }
}
