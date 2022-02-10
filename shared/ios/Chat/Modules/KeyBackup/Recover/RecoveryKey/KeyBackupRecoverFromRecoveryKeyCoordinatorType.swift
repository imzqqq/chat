import Foundation

protocol KeyBackupRecoverFromRecoveryKeyCoordinatorDelegate: AnyObject {
    func keyBackupRecoverFromPassphraseCoordinatorDidRecover(_ keyBackupRecoverFromRecoveryKeyCoordinator: KeyBackupRecoverFromRecoveryKeyCoordinatorType)
    func keyBackupRecoverFromPassphraseCoordinatorDidCancel(_ keyBackupRecoverFromRecoveryKeyCoordinator: KeyBackupRecoverFromRecoveryKeyCoordinatorType)
}

/// `KeyBackupRecoverFromRecoveryKeyCoordinatorType` is a protocol describing a Coordinator that handle key backup recover from recovery key navigation flow.
protocol KeyBackupRecoverFromRecoveryKeyCoordinatorType: Coordinator, Presentable {
    var delegate: KeyBackupRecoverFromRecoveryKeyCoordinatorDelegate? { get }
}
