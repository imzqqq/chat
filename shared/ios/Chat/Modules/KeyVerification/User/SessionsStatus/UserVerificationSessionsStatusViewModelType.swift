// File created from ScreenTemplate
// $ createScreen.sh UserVerification UserVerificationSessionsStatus

import Foundation

protocol UserVerificationSessionsStatusViewModelViewDelegate: AnyObject {
    func userVerificationSessionsStatusViewModel(_ viewModel: UserVerificationSessionsStatusViewModelType, didUpdateViewState viewSate: UserVerificationSessionsStatusViewState)
}

protocol UserVerificationSessionsStatusViewModelCoordinatorDelegate: AnyObject {
    func userVerificationSessionsStatusViewModel(_ viewModel: UserVerificationSessionsStatusViewModelType, didSelectDeviceWithId deviceId: String, for userId: String)
    func userVerificationSessionsStatusViewModelDidClose(_ viewModel: UserVerificationSessionsStatusViewModelType)
}

/// Protocol describing the view model used by `UserVerificationSessionsStatusViewController`
protocol UserVerificationSessionsStatusViewModelType {
            
    var viewDelegate: UserVerificationSessionsStatusViewModelViewDelegate? { get set }
    var coordinatorDelegate: UserVerificationSessionsStatusViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: UserVerificationSessionsStatusViewAction)
}
