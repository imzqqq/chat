// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Incoming DeviceVerificationIncoming

import Foundation

protocol DeviceVerificationIncomingViewModelViewDelegate: AnyObject {
    func deviceVerificationIncomingViewModel(_ viewModel: DeviceVerificationIncomingViewModelType, didUpdateViewState viewSate: DeviceVerificationIncomingViewState)
}

protocol DeviceVerificationIncomingViewModelCoordinatorDelegate: AnyObject {
    func deviceVerificationIncomingViewModel(_ viewModel: DeviceVerificationIncomingViewModelType, didAcceptTransaction transaction: MXSASTransaction)    
    func deviceVerificationIncomingViewModelDidCancel(_ viewModel: DeviceVerificationIncomingViewModelType)
}

/// Protocol describing the view model used by `DeviceVerificationIncomingViewController`
protocol DeviceVerificationIncomingViewModelType {

    var userId: String { get }
    var userDisplayName: String? { get }
    var avatarUrl: String? { get }
    var deviceId: String { get }

    var mediaManager: MXMediaManager { get }
        
    var viewDelegate: DeviceVerificationIncomingViewModelViewDelegate? { get set }
    var coordinatorDelegate: DeviceVerificationIncomingViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: DeviceVerificationIncomingViewAction)
}
