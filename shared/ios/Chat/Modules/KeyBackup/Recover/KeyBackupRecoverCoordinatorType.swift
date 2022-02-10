import Foundation

protocol KeyBackupRecoverCoordinatorDelegate: AnyObject {
    func keyBackupRecoverCoordinatorDidRecover(_ keyBackupRecoverCoordinator: KeyBackupRecoverCoordinatorType)
    func keyBackupRecoverCoordinatorDidCancel(_ keyBackupRecoverCoordinator: KeyBackupRecoverCoordinatorType)
}

/// `KeyBackupSetupPassphraseCoordinatorType` is a protocol describing a Coordinator that handle key backup recover navigation flow.
protocol KeyBackupRecoverCoordinatorType: Coordinator, Presentable {
    var delegate: KeyBackupRecoverCoordinatorDelegate? { get }
}
