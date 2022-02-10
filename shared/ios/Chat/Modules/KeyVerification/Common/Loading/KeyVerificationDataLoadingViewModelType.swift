// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Loading DeviceVerificationDataLoading

import Foundation

protocol KeyVerificationDataLoadingViewModelViewDelegate: AnyObject {
    func keyVerificationDataLoadingViewModel(_ viewModel: KeyVerificationDataLoadingViewModelType, didUpdateViewState viewSate: KeyVerificationDataLoadingViewState)
}

protocol KeyVerificationDataLoadingViewModelCoordinatorDelegate: AnyObject {
    func keyVerificationDataLoadingViewModel(_ viewModel: KeyVerificationDataLoadingViewModelType, didLoadUser user: MXUser, device: MXDeviceInfo)
    func keyVerificationDataLoadingViewModel(_ viewModel: KeyVerificationDataLoadingViewModelType, didAcceptKeyVerificationWithTransaction transaction: MXKeyVerificationTransaction)
    func keyVerificationDataLoadingViewModel(_ viewModel: KeyVerificationDataLoadingViewModelType, didAcceptKeyVerificationRequest keyVerificationRequest: MXKeyVerificationRequest)
    func keyVerificationDataLoadingViewModelDidCancel(_ viewModel: KeyVerificationDataLoadingViewModelType)
}

/// Protocol describing the view model used by `KeyVerificationDataLoadingViewController`
protocol KeyVerificationDataLoadingViewModelType {
        
    var viewDelegate: KeyVerificationDataLoadingViewModelViewDelegate? { get set }
    var coordinatorDelegate: KeyVerificationDataLoadingViewModelCoordinatorDelegate? { get set }
    
    var verificationKind: KeyVerificationKind { get }
    
    func process(viewAction: KeyVerificationDataLoadingViewAction)
}
