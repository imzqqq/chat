import Foundation

protocol SecretsRecoveryWithPassphraseViewModelViewDelegate: AnyObject {
    func secretsRecoveryWithPassphraseViewModel(_ viewModel: SecretsRecoveryWithPassphraseViewModelType, didUpdateViewState viewSate: SecretsRecoveryWithPassphraseViewState)
}

protocol SecretsRecoveryWithPassphraseViewModelCoordinatorDelegate: AnyObject {
    func secretsRecoveryWithPassphraseViewModelDidRecover(_ viewModel: SecretsRecoveryWithPassphraseViewModelType)
    func secretsRecoveryWithPassphraseViewModelDidCancel(_ viewModel: SecretsRecoveryWithPassphraseViewModelType)
    func secretsRecoveryWithPassphraseViewModelWantsToRecoverByKey(_ viewModel: SecretsRecoveryWithPassphraseViewModelType)
    func secretsRecoveryWithPassphraseViewModelWantsToResetSecrets(_ viewModel: SecretsRecoveryWithPassphraseViewModelType)
}

/// Protocol describing the view model used by `SecretsRecoveryWithPassphraseViewController`
protocol SecretsRecoveryWithPassphraseViewModelType {
    
    var passphrase: String? { get set }
    var isFormValid: Bool { get }
    var recoveryGoal: SecretsRecoveryGoal { get }
    
    var viewDelegate: SecretsRecoveryWithPassphraseViewModelViewDelegate? { get set }
    var coordinatorDelegate: SecretsRecoveryWithPassphraseViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SecretsRecoveryWithPassphraseViewAction)
}
