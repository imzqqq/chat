import Foundation
import UIKit

final class KeyBackupSetupPassphraseCoordinator: KeyBackupSetupPassphraseCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private var keyBackupSetupPassphraseViewModel: KeyBackupSetupPassphraseViewModelType
    private let keyBackupSetupPassphraseViewController: KeyBackupSetupPassphraseViewController
    
    // MARK: Public
    
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: KeyBackupSetupPassphraseCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession) {
        self.session = session
        
        let keyBackupSetupPassphraseViewModel = KeyBackupSetupPassphraseViewModel(keyBackup: self.session.crypto.backup)
        let keyBackupSetupPassphraseViewController = KeyBackupSetupPassphraseViewController.instantiate(with: keyBackupSetupPassphraseViewModel)
        self.keyBackupSetupPassphraseViewModel = keyBackupSetupPassphraseViewModel
        self.keyBackupSetupPassphraseViewController = keyBackupSetupPassphraseViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.keyBackupSetupPassphraseViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.keyBackupSetupPassphraseViewController
    }
}

// MARK: - KeyBackupSetupPassphraseViewModelCoordinatorDelegate
extension KeyBackupSetupPassphraseCoordinator: KeyBackupSetupPassphraseViewModelCoordinatorDelegate {
    
    func keyBackupSetupPassphraseViewModel(_ viewModel: KeyBackupSetupPassphraseViewModelType, didCreateBackupFromPassphraseWithResultingRecoveryKey recoveryKey: String) {
        self.delegate?.keyBackupSetupPassphraseCoordinator(self, didCreateBackupFromPassphraseWithResultingRecoveryKey: recoveryKey)
    }
    
    func keyBackupSetupPassphraseViewModel(_ viewModel: KeyBackupSetupPassphraseViewModelType, didCreateBackupFromRecoveryKey recoveryKey: String) {
        self.delegate?.keyBackupSetupPassphraseCoordinator(self, didCreateBackupFromRecoveryKey: recoveryKey)
    }
    
    func keyBackupSetupPassphraseViewModelDidCancel(_ viewModel: KeyBackupSetupPassphraseViewModelType) {
        self.delegate?.keyBackupSetupPassphraseCoordinatorDidCancel(self)
    }
}
