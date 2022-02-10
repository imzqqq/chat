// File created from ScreenTemplate
// $ createScreen.sh SecretsSetupRecoveryKey SecretsSetupRecoveryKey

import Foundation
import UIKit

final class SecretsSetupRecoveryKeyCoordinator: SecretsSetupRecoveryKeyCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private var secretsSetupRecoveryKeyViewModel: SecretsSetupRecoveryKeyViewModelType
    private let secretsSetupRecoveryKeyViewController: SecretsSetupRecoveryKeyViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: SecretsSetupRecoveryKeyCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(recoveryService: MXRecoveryService,
         passphrase: String?,
         passphraseOnly: Bool,
         allowOverwrite: Bool = false) {
        let secretsSetupRecoveryKeyViewModel = SecretsSetupRecoveryKeyViewModel(recoveryService: recoveryService, passphrase: passphrase, passphraseOnly: passphraseOnly, allowOverwrite: allowOverwrite)
        let secretsSetupRecoveryKeyViewController = SecretsSetupRecoveryKeyViewController.instantiate(with: secretsSetupRecoveryKeyViewModel)
        self.secretsSetupRecoveryKeyViewModel = secretsSetupRecoveryKeyViewModel
        self.secretsSetupRecoveryKeyViewController = secretsSetupRecoveryKeyViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.secretsSetupRecoveryKeyViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.secretsSetupRecoveryKeyViewController
    }
}

// MARK: - SecretsSetupRecoveryKeyViewModelCoordinatorDelegate
extension SecretsSetupRecoveryKeyCoordinator: SecretsSetupRecoveryKeyViewModelCoordinatorDelegate {
    
    func secretsSetupRecoveryKeyViewModelDidComplete(_ viewModel: SecretsSetupRecoveryKeyViewModelType) {
        self.delegate?.secretsSetupRecoveryKeyCoordinatorDidComplete(self)
    }
    
    func secretsSetupRecoveryKeyViewModelDidFailed(_ viewModel: SecretsSetupRecoveryKeyViewModelType) {
        self.delegate?.secretsSetupRecoveryKeyCoordinatorDidFailed(self)
    }
    
    func secretsSetupRecoveryKeyViewModelDidCancel(_ viewModel: SecretsSetupRecoveryKeyViewModelType) {
        self.delegate?.secretsSetupRecoveryKeyCoordinatorDidCancel(self)
    }
}
