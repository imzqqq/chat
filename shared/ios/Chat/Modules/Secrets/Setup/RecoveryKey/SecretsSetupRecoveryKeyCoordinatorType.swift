// File created from ScreenTemplate
// $ createScreen.sh SecretsSetupRecoveryKey SecretsSetupRecoveryKey

import Foundation

protocol SecretsSetupRecoveryKeyCoordinatorDelegate: AnyObject {
    func secretsSetupRecoveryKeyCoordinatorDidComplete(_ coordinator: SecretsSetupRecoveryKeyCoordinatorType)
    func secretsSetupRecoveryKeyCoordinatorDidFailed(_ coordinator: SecretsSetupRecoveryKeyCoordinatorType)
    func secretsSetupRecoveryKeyCoordinatorDidCancel(_ coordinator: SecretsSetupRecoveryKeyCoordinatorType)
}

/// `SecretsSetupRecoveryKeyCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SecretsSetupRecoveryKeyCoordinatorType: Coordinator, Presentable {
    var delegate: SecretsSetupRecoveryKeyCoordinatorDelegate? { get }
}
