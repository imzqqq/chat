// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Start DeviceVerificationStart

import Foundation

protocol DeviceVerificationStartViewModelViewDelegate: AnyObject {
    func deviceVerificationStartViewModel(_ viewModel: DeviceVerificationStartViewModelType, didUpdateViewState viewSate: DeviceVerificationStartViewState)
}

protocol DeviceVerificationStartViewModelCoordinatorDelegate: AnyObject {
    func deviceVerificationStartViewModelDidUseLegacyVerification(_ viewModel: DeviceVerificationStartViewModelType)

    func deviceVerificationStartViewModel(_ viewModel: DeviceVerificationStartViewModelType, didCompleteWithOutgoingTransaction transaction: MXSASTransaction)
    func deviceVerificationStartViewModel(_ viewModel: DeviceVerificationStartViewModelType, didTransactionCancelled transaction: MXSASTransaction)

    func deviceVerificationStartViewModelDidCancel(_ viewModel: DeviceVerificationStartViewModelType)
}

/// Protocol describing the view model used by `DeviceVerificationStartViewController`
protocol DeviceVerificationStartViewModelType {        
    var viewDelegate: DeviceVerificationStartViewModelViewDelegate? { get set }
    var coordinatorDelegate: DeviceVerificationStartViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: DeviceVerificationStartViewAction)
}
