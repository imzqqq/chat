// File created from ScreenTemplate
// $ createScreen.sh KeyVerification KeyVerificationSelfVerifyWait

import Foundation
import UIKit

final class KeyVerificationSelfVerifyWaitCoordinator: KeyVerificationSelfVerifyWaitCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private var keyVerificationSelfVerifyWaitViewModel: KeyVerificationSelfVerifyWaitViewModelType
    private let keyVerificationSelfVerifyWaitViewController: KeyVerificationSelfVerifyWaitViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: KeyVerificationSelfVerifyWaitCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession, isNewSignIn: Bool) {
        self.session = session
        
        let keyVerificationSelfVerifyWaitViewModel = KeyVerificationSelfVerifyWaitViewModel(session: self.session, isNewSignIn: isNewSignIn)
        let keyVerificationSelfVerifyWaitViewController = KeyVerificationSelfVerifyWaitViewController.instantiate(with: keyVerificationSelfVerifyWaitViewModel)
        self.keyVerificationSelfVerifyWaitViewModel = keyVerificationSelfVerifyWaitViewModel
        self.keyVerificationSelfVerifyWaitViewController = keyVerificationSelfVerifyWaitViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.keyVerificationSelfVerifyWaitViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.keyVerificationSelfVerifyWaitViewController
    }
}

// MARK: - KeyVerificationSelfVerifyWaitViewModelCoordinatorDelegate
extension KeyVerificationSelfVerifyWaitCoordinator: KeyVerificationSelfVerifyWaitViewModelCoordinatorDelegate {
    
    func keyVerificationSelfVerifyWaitViewModel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType, didAcceptKeyVerificationRequest keyVerificationRequest: MXKeyVerificationRequest) {
        self.delegate?.keyVerificationSelfVerifyWaitCoordinator(self, didAcceptKeyVerificationRequest: keyVerificationRequest)
    }
    
    func keyVerificationSelfVerifyWaitViewModel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType, didAcceptIncomingSASTransaction incomingSASTransaction: MXIncomingSASTransaction) {
        self.delegate?.keyVerificationSelfVerifyWaitCoordinator(self, didAcceptIncomingSASTransaction: incomingSASTransaction)
    }
    
    func keyVerificationSelfVerifyWaitViewModelDidCancel(_ viewModel: KeyVerificationSelfVerifyWaitViewModelType) {
        self.delegate?.keyVerificationSelfVerifyWaitCoordinatorDidCancel(self)
    }
    
    func keyVerificationSelfVerifyWaitViewModel(_ coordinator: KeyVerificationSelfVerifyWaitViewModelType, wantsToRecoverSecretsWith secretsRecoveryMode: SecretsRecoveryMode) {
        self.delegate?.keyVerificationSelfVerifyWaitCoordinator(self, wantsToRecoverSecretsWith: secretsRecoveryMode)
    }
}
