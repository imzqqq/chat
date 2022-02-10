import Foundation

final class SecretsRecoveryWithPassphraseCoordinator: SecretsRecoveryWithPassphraseCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let secretsRecoveryWithPassphraseViewController: SecretsRecoveryWithPassphraseViewController
    private var secretsRecoveryWithPassphraseViewModel: SecretsRecoveryWithPassphraseViewModelType
    
    // MARK: Public
    
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: SecretsRecoveryWithPassphraseCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(recoveryService: MXRecoveryService, recoveryGoal: SecretsRecoveryGoal) {
        let secretsRecoveryWithPassphraseViewModel = SecretsRecoveryWithPassphraseViewModel(recoveryService: recoveryService, recoveryGoal: recoveryGoal)
        let secretsRecoveryWithPassphraseViewController = SecretsRecoveryWithPassphraseViewController.instantiate(with: secretsRecoveryWithPassphraseViewModel)
        self.secretsRecoveryWithPassphraseViewController = secretsRecoveryWithPassphraseViewController
        self.secretsRecoveryWithPassphraseViewModel = secretsRecoveryWithPassphraseViewModel
    }
    
    // MARK: - Public
    
    func start() {
        self.secretsRecoveryWithPassphraseViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.secretsRecoveryWithPassphraseViewController
    }
}

// MARK: - SecretsRecoveryWithPassphraseViewModelCoordinatorDelegate
extension SecretsRecoveryWithPassphraseCoordinator: SecretsRecoveryWithPassphraseViewModelCoordinatorDelegate {
    func secretsRecoveryWithPassphraseViewModelWantsToRecoverByKey(_ viewModel: SecretsRecoveryWithPassphraseViewModelType) {
        self.delegate?.secretsRecoveryWithPassphraseCoordinatorDoNotKnowPassphrase(self)
    }
    
    func secretsRecoveryWithPassphraseViewModelDidRecover(_ viewModel: SecretsRecoveryWithPassphraseViewModelType) {
        self.delegate?.secretsRecoveryWithPassphraseCoordinatorDidRecover(self)
    }
    
    func secretsRecoveryWithPassphraseViewModelDidCancel(_ viewModel: SecretsRecoveryWithPassphraseViewModelType) {
        self.delegate?.secretsRecoveryWithPassphraseCoordinatorDidCancel(self)
    }
    
    func secretsRecoveryWithPassphraseViewModelWantsToResetSecrets(_ viewModel: SecretsRecoveryWithPassphraseViewModelType) {
        self.delegate?.secretsRecoveryWithPassphraseCoordinatorWantsToResetSecrets(self)
    }
}
