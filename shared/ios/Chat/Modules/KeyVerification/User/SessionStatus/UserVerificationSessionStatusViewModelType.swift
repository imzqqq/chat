// File created from ScreenTemplate
// $ createScreen.sh SessionStatus UserVerificationSessionStatus

import Foundation

protocol UserVerificationSessionStatusViewModelViewDelegate: AnyObject {
    func userVerificationSessionStatusViewModel(_ viewModel: UserVerificationSessionStatusViewModelType, didUpdateViewState viewSate: UserVerificationSessionStatusViewState)
}

protocol UserVerificationSessionStatusViewModelCoordinatorDelegate: AnyObject {
    func userVerificationSessionStatusViewModel(_ viewModel: UserVerificationSessionStatusViewModelType, wantsToVerifyDeviceWithId deviceId: String, for userId: String)
    func userVerificationSessionStatusViewModel(_ viewModel: UserVerificationSessionStatusViewModelType, wantsToManuallyVerifyDeviceWithId deviceId: String, for userId: String)
    func userVerificationSessionStatusViewModelDidClose(_ viewModel: UserVerificationSessionStatusViewModelType)
}

/// Protocol describing the view model used by `UserVerificationSessionStatusViewController`
protocol UserVerificationSessionStatusViewModelType {        
        
    var viewDelegate: UserVerificationSessionStatusViewModelViewDelegate? { get set }
    var coordinatorDelegate: UserVerificationSessionStatusViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: UserVerificationSessionStatusViewAction)
}
