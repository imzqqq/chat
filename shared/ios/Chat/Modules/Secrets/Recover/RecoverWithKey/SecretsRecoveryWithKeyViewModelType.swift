import Foundation

protocol SecretsRecoveryWithKeyViewModelViewDelegate: AnyObject {
    func secretsRecoveryWithKeyViewModel(_ viewModel: SecretsRecoveryWithKeyViewModelType, didUpdateViewState viewSate: SecretsRecoveryWithKeyViewState)
}

protocol SecretsRecoveryWithKeyViewModelCoordinatorDelegate: AnyObject {
    func secretsRecoveryWithKeyViewModelDidRecover(_ viewModel: SecretsRecoveryWithKeyViewModelType)
    func secretsRecoveryWithKeyViewModelDidCancel(_ viewModel: SecretsRecoveryWithKeyViewModelType)
    func secretsRecoveryWithKeyViewModelWantsToResetSecrets(_ viewModel: SecretsRecoveryWithKeyViewModelType)
}

/// Protocol describing the view model used by `SecretsRecoveryWithPassphraseViewController`
protocol SecretsRecoveryWithKeyViewModelType {
    
    var recoveryKey: String? { get set }
    var isFormValid: Bool { get }
    var recoveryGoal: SecretsRecoveryGoal { get }
    
    var viewDelegate: SecretsRecoveryWithKeyViewModelViewDelegate? { get set }
    var coordinatorDelegate: SecretsRecoveryWithKeyViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SecretsRecoveryWithKeyViewAction)
}
