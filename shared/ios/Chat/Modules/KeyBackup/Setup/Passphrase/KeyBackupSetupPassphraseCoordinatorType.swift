import Foundation

protocol KeyBackupSetupPassphraseCoordinatorDelegate: AnyObject {
    func keyBackupSetupPassphraseCoordinator(_ keyBackupSetupPassphraseCoordinator: KeyBackupSetupPassphraseCoordinatorType, didCreateBackupFromPassphraseWithResultingRecoveryKey recoveryKey: String)
    func keyBackupSetupPassphraseCoordinator(_ keyBackupSetupPassphraseCoordinator: KeyBackupSetupPassphraseCoordinatorType, didCreateBackupFromRecoveryKey recoveryKey: String)
    func keyBackupSetupPassphraseCoordinatorDidCancel(_ keyBackupSetupPassphraseCoordinator: KeyBackupSetupPassphraseCoordinatorType)
}

/// `KeyBackupSetupPassphraseCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol KeyBackupSetupPassphraseCoordinatorType: Coordinator, Presentable {
    var delegate: KeyBackupSetupPassphraseCoordinatorDelegate? { get }
}
