// File created from ScreenTemplate
// $ createScreen.sh KeyVerification/Device/ManuallyVerify KeyVerificationManuallyVerify

import Foundation

protocol KeyVerificationManuallyVerifyViewModelViewDelegate: AnyObject {
    func keyVerificationManuallyVerifyViewModel(_ viewModel: KeyVerificationManuallyVerifyViewModelType, didUpdateViewState viewSate: KeyVerificationManuallyVerifyViewState)
}

protocol KeyVerificationManuallyVerifyViewModelCoordinatorDelegate: AnyObject {
    func keyVerificationManuallyVerifyViewModel(_ viewModel: KeyVerificationManuallyVerifyViewModelType, didVerifiedDeviceWithId deviceId: String, of userId: String)
    func keyVerificationManuallyVerifyViewModelDidCancel(_ viewModel: KeyVerificationManuallyVerifyViewModelType)
}

/// Protocol describing the view model used by `KeyVerificationManuallyVerifyViewController`
protocol KeyVerificationManuallyVerifyViewModelType {        
        
    var viewDelegate: KeyVerificationManuallyVerifyViewModelViewDelegate? { get set }
    var coordinatorDelegate: KeyVerificationManuallyVerifyViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: KeyVerificationManuallyVerifyViewAction)
}
