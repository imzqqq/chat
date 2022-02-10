// File created from ScreenTemplate
// $ createScreen.sh KeyVerification KeyVerificationSelfVerifyStart

import Foundation

protocol KeyVerificationSelfVerifyStartViewModelViewDelegate: AnyObject {
    func keyVerificationSelfVerifyStartViewModel(_ viewModel: KeyVerificationSelfVerifyStartViewModelType, didUpdateViewState viewSate: KeyVerificationSelfVerifyStartViewState)
}

protocol KeyVerificationSelfVerifyStartViewModelCoordinatorDelegate: AnyObject {
    func keyVerificationSelfVerifyStartViewModel(_ viewModel: KeyVerificationSelfVerifyStartViewModelType, otherDidAcceptRequest request: MXKeyVerificationRequest)
    func keyVerificationSelfVerifyStartViewModelDidCancel(_ viewModel: KeyVerificationSelfVerifyStartViewModelType)
}

/// Protocol describing the view model used by `KeyVerificationSelfVerifyStartViewController`
protocol KeyVerificationSelfVerifyStartViewModelType {
            
    var viewDelegate: KeyVerificationSelfVerifyStartViewModelViewDelegate? { get set }
    var coordinatorDelegate: KeyVerificationSelfVerifyStartViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: KeyVerificationSelfVerifyStartViewAction)
}
