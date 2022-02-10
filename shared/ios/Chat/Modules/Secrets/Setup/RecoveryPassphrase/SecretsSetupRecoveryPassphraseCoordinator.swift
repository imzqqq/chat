// File created from ScreenTemplate
// $ createScreen.sh Test SecretsSetupRecoveryPassphrase

import Foundation
import UIKit

final class SecretsSetupRecoveryPassphraseCoordinator: SecretsSetupRecoveryPassphraseCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
        
    private var secretsSetupRecoveryPassphraseViewModel: SecretsSetupRecoveryPassphraseViewModelType
    private let secretsSetupRecoveryPassphraseViewController: SecretsSetupRecoveryPassphraseViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: SecretsSetupRecoveryPassphraseCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(passphraseInput: SecretsSetupRecoveryPassphraseInput) {
        
        let secretsSetupRecoveryPassphraseViewModel = SecretsSetupRecoveryPassphraseViewModel(passphraseInput: passphraseInput)
        let secretsSetupRecoveryPassphraseViewController = SecretsSetupRecoveryPassphraseViewController.instantiate(with: secretsSetupRecoveryPassphraseViewModel)
        self.secretsSetupRecoveryPassphraseViewModel = secretsSetupRecoveryPassphraseViewModel
        self.secretsSetupRecoveryPassphraseViewController = secretsSetupRecoveryPassphraseViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.secretsSetupRecoveryPassphraseViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.secretsSetupRecoveryPassphraseViewController
    }
}

// MARK: - SecretsSetupRecoveryPassphraseViewModelCoordinatorDelegate
extension SecretsSetupRecoveryPassphraseCoordinator: SecretsSetupRecoveryPassphraseViewModelCoordinatorDelegate {
    
    func secretsSetupRecoveryPassphraseViewModel(_ viewModel: SecretsSetupRecoveryPassphraseViewModelType, didEnterNewPassphrase passphrase: String) {
        self.delegate?.secretsSetupRecoveryPassphraseCoordinator(self, didEnterNewPassphrase: passphrase)
    }
    
    func secretsSetupRecoveryPassphraseViewModel(_ viewModel: SecretsSetupRecoveryPassphraseViewModelType, didConfirmPassphrase passphrase: String) {
        self.delegate?.secretsSetupRecoveryPassphraseCoordinator(self, didConfirmPassphrase: passphrase)
    }
    
    func secretsSetupRecoveryPassphraseViewModelDidCancel(_ viewModel: SecretsSetupRecoveryPassphraseViewModelType) {
        self.delegate?.secretsSetupRecoveryPassphraseCoordinatorDidCancel(self)
    }
}
