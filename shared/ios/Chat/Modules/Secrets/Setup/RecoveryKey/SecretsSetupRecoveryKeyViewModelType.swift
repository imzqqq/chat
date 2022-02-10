// File created from ScreenTemplate
// $ createScreen.sh SecretsSetupRecoveryKey SecretsSetupRecoveryKey

import Foundation

protocol SecretsSetupRecoveryKeyViewModelViewDelegate: AnyObject {
    func secretsSetupRecoveryKeyViewModel(_ viewModel: SecretsSetupRecoveryKeyViewModelType, didUpdateViewState viewSate: SecretsSetupRecoveryKeyViewState)
}

protocol SecretsSetupRecoveryKeyViewModelCoordinatorDelegate: AnyObject {
    func secretsSetupRecoveryKeyViewModelDidComplete(_ viewModel: SecretsSetupRecoveryKeyViewModelType)
    func secretsSetupRecoveryKeyViewModelDidFailed(_ viewModel: SecretsSetupRecoveryKeyViewModelType)
    func secretsSetupRecoveryKeyViewModelDidCancel(_ viewModel: SecretsSetupRecoveryKeyViewModelType)
}

/// Protocol describing the view model used by `SecretsSetupRecoveryKeyViewController`
protocol SecretsSetupRecoveryKeyViewModelType {        
        
    var viewDelegate: SecretsSetupRecoveryKeyViewModelViewDelegate? { get set }
    var coordinatorDelegate: SecretsSetupRecoveryKeyViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SecretsSetupRecoveryKeyViewAction)
}
