// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Verify DeviceVerificationVerify

import Foundation

protocol KeyVerificationVerifyBySASViewModelViewDelegate: AnyObject {
    func keyVerificationVerifyBySASViewModel(_ viewModel: KeyVerificationVerifyBySASViewModelType, didUpdateViewState viewSate: KeyVerificationVerifyViewState)
}

protocol KeyVerificationVerifyBySASViewModelCoordinatorDelegate: AnyObject {
    func keyVerificationVerifyViewModelDidComplete(_ viewModel: KeyVerificationVerifyBySASViewModelType)    
    func keyVerificationVerifyViewModelDidCancel(_ viewModel: KeyVerificationVerifyBySASViewModelType)
}

/// Protocol describing the view model used by `KeyVerificationVerifyBySASViewController`
protocol KeyVerificationVerifyBySASViewModelType {
        
    var viewDelegate: KeyVerificationVerifyBySASViewModelViewDelegate? { get set }
    var coordinatorDelegate: KeyVerificationVerifyBySASViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: KeyVerificationVerifyBySASViewAction)

    var emojis: [MXEmojiRepresentation]? { get }
    var decimal: String? { get }
    var verificationKind: KeyVerificationKind { get }
}
