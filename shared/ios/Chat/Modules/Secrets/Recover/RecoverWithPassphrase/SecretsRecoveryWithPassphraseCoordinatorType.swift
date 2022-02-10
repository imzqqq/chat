import Foundation

protocol SecretsRecoveryWithPassphraseCoordinatorDelegate: AnyObject {
    func secretsRecoveryWithPassphraseCoordinatorDidRecover(_ coordinator: SecretsRecoveryWithPassphraseCoordinatorType)
    func secretsRecoveryWithPassphraseCoordinatorDoNotKnowPassphrase(_ coordinator: SecretsRecoveryWithPassphraseCoordinatorType)
    func secretsRecoveryWithPassphraseCoordinatorDidCancel(_ coordinator: SecretsRecoveryWithPassphraseCoordinatorType)
    func secretsRecoveryWithPassphraseCoordinatorWantsToResetSecrets(_ coordinator: SecretsRecoveryWithPassphraseCoordinatorType)
}

/// `SecretsRecoveryWithPassphraseCoordinatorType` is a protocol describing a Coordinator that handle key backup passphrase recover navigation flow.
protocol SecretsRecoveryWithPassphraseCoordinatorType: Coordinator, Presentable {
    var delegate: SecretsRecoveryWithPassphraseCoordinatorDelegate? { get }
}
