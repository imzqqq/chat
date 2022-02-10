// File created from ScreenTemplate
// $ createScreen.sh .KeyBackup/Recover/PrivateKey KeyBackupRecoverFromPrivateKey

import Foundation

protocol KeyBackupRecoverFromPrivateKeyCoordinatorDelegate: AnyObject {
    func keyBackupRecoverFromPrivateKeyCoordinatorDidRecover(_ coordinator: KeyBackupRecoverFromPrivateKeyCoordinatorType)
    func keyBackupRecoverFromPrivateKeyCoordinatorDidPrivateKeyFail(_ coordinator: KeyBackupRecoverFromPrivateKeyCoordinatorType)
    func keyBackupRecoverFromPrivateKeyCoordinatorDidCancel(_ coordinator: KeyBackupRecoverFromPrivateKeyCoordinatorType)
}

/// `KeyBackupRecoverFromPrivateKeyCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol KeyBackupRecoverFromPrivateKeyCoordinatorType: Coordinator, Presentable {
    var delegate: KeyBackupRecoverFromPrivateKeyCoordinatorDelegate? { get }
}
