// File created from ScreenTemplate
// $ createScreen.sh Start UserVerificationStart

import Foundation

protocol UserVerificationStartViewModelViewDelegate: AnyObject {
    func userVerificationStartViewModel(_ viewModel: UserVerificationStartViewModelType, didUpdateViewState viewSate: UserVerificationStartViewState)
}

protocol UserVerificationStartViewModelCoordinatorDelegate: AnyObject {
    
    func userVerificationStartViewModel(_ viewModel: UserVerificationStartViewModelType, otherDidAcceptRequest request: MXKeyVerificationRequest)
    
    func userVerificationStartViewModelDidCancel(_ viewModel: UserVerificationStartViewModelType)
}

/// Protocol describing the view model used by `UserVerificationStartViewController`
protocol UserVerificationStartViewModelType {
            
    var viewDelegate: UserVerificationStartViewModelViewDelegate? { get set }
    var coordinatorDelegate: UserVerificationStartViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: UserVerificationStartViewAction)
}
