import Foundation

final class SecretsRecoveryWithKeyCoordinator: SecretsRecoveryWithKeyCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let secretsRecoveryWithKeyViewController: SecretsRecoveryWithKeyViewController
    private let secretsRecoveryWithKeyViewModel: SecretsRecoveryWithKeyViewModel
    
    // MARK: Public
    
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: SecretsRecoveryWithKeyCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(recoveryService: MXRecoveryService, recoveryGoal: SecretsRecoveryGoal) {
        
        let secretsRecoveryWithKeyViewModel = SecretsRecoveryWithKeyViewModel(recoveryService: recoveryService, recoveryGoal: recoveryGoal)
        let secretsRecoveryWithKeyViewController = SecretsRecoveryWithKeyViewController.instantiate(with: secretsRecoveryWithKeyViewModel)
        self.secretsRecoveryWithKeyViewController = secretsRecoveryWithKeyViewController
        self.secretsRecoveryWithKeyViewModel = secretsRecoveryWithKeyViewModel
    }
    
    // MARK: - Public
    
    func start() {
        self.secretsRecoveryWithKeyViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.secretsRecoveryWithKeyViewController
    }
}

// MARK: - secretsRecoveryWithKeyViewModelCoordinatorDelegate
extension SecretsRecoveryWithKeyCoordinator: SecretsRecoveryWithKeyViewModelCoordinatorDelegate {
    func secretsRecoveryWithKeyViewModelDidRecover(_ viewModel: SecretsRecoveryWithKeyViewModelType) {        self.delegate?.secretsRecoveryWithKeyCoordinatorDidRecover(self)
    }
    
    func secretsRecoveryWithKeyViewModelDidCancel(_ viewModel: SecretsRecoveryWithKeyViewModelType) {        self.delegate?.secretsRecoveryWithKeyCoordinatorDidCancel(self)
    }
    
    func secretsRecoveryWithKeyViewModelWantsToResetSecrets(_ viewModel: SecretsRecoveryWithKeyViewModelType) {
        self.delegate?.secretsRecoveryWithKeyCoordinatorWantsToResetSecrets(self)
    }
}
