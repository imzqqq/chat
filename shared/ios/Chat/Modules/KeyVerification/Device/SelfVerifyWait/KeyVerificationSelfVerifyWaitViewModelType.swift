// File created from ScreenTemplate
// $ createScreen.sh KeyVerification KeyVerificationSelfVerifyWait

import Foundation

protocol KeyVerificationSelfVerifyWaitViewModelViewDelegate: AnyObject {
    func keyVerificationSelfVerifyWaitViewModel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType, didUpdateViewState viewSate: KeyVerificationSelfVerifyWaitViewState)
}

protocol KeyVerificationSelfVerifyWaitViewModelCoordinatorDelegate: AnyObject {
    func keyVerificationSelfVerifyWaitViewModel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType, didAcceptKeyVerificationRequest keyVerificationRequest: MXKeyVerificationRequest)
    func keyVerificationSelfVerifyWaitViewModel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType, didAcceptIncomingSASTransaction incomingSASTransaction: MXIncomingSASTransaction)
    func keyVerificationSelfVerifyWaitViewModelDidCancel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType)
    func keyVerificationSelfVerifyWaitViewModel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType, wantsToRecoverSecretsWith secretsRecoveryMode: SecretsRecoveryMode)
}

/// Protocol describing the view model used by `KeyVerificationSelfVerifyWaitViewController`
protocol KeyVerificationSelfVerifyWaitViewModelType {        
        
    var viewDelegate: KeyVerificationSelfVerifyWaitViewModelViewDelegate? { get set }
    var coordinatorDelegate: KeyVerificationSelfVerifyWaitViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: KeyVerificationSelfVerifyWaitViewAction)
}
