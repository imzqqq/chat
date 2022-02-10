import Foundation

protocol SecretsRecoveryWithKeyCoordinatorDelegate: AnyObject {
    func secretsRecoveryWithKeyCoordinatorDidRecover(_ coordinator: SecretsRecoveryWithKeyCoordinatorType)
    func secretsRecoveryWithKeyCoordinatorDidCancel(_ coordinator: SecretsRecoveryWithKeyCoordinatorType)
    func secretsRecoveryWithKeyCoordinatorWantsToResetSecrets(_ viewModel: SecretsRecoveryWithKeyCoordinatorType)
}

/// `SecretsRecoveryWithKeyCoordinatorType` is a protocol describing a Coordinator that handle secrets recovery from recovery key navigation flow.
protocol SecretsRecoveryWithKeyCoordinatorType: Coordinator, Presentable {
    var delegate: SecretsRecoveryWithKeyCoordinatorDelegate? { get }
}
