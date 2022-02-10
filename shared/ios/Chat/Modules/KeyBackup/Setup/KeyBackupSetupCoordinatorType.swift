import Foundation

protocol KeyBackupSetupCoordinatorDelegate: AnyObject {
    func keyBackupSetupCoordinatorDidCancel(_ keyBackupSetupCoordinator: KeyBackupSetupCoordinatorType)
    func keyBackupSetupCoordinatorDidSetupRecoveryKey(_ keyBackupSetupCoordinator: KeyBackupSetupCoordinatorType)
}

/// `KeyBackupSetupCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol KeyBackupSetupCoordinatorType: Coordinator, Presentable {
    var delegate: KeyBackupSetupCoordinatorDelegate? { get }
}
