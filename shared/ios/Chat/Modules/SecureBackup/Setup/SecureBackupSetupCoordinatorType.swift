// File created from FlowTemplate
// $ createRootCoordinator.sh KeyBackupSetup/SecureSetup SecureKeyBackupSetup

import Foundation

protocol SecureBackupSetupCoordinatorDelegate: AnyObject {
    func secureBackupSetupCoordinatorDidComplete(_ coordinator: SecureBackupSetupCoordinatorType)
    func secureBackupSetupCoordinatorDidCancel(_ coordinator: SecureBackupSetupCoordinatorType)
}

/// `SecureBackupSetupCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol SecureBackupSetupCoordinatorType: Coordinator, Presentable {
    var delegate: SecureBackupSetupCoordinatorDelegate? { get }
}
