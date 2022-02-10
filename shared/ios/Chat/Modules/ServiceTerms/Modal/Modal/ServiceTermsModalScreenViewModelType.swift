// File created from ScreenTemplate
// $ createScreen.sh Modal/Show ServiceTermsModalScreen

import Foundation

protocol ServiceTermsModalScreenViewModelViewDelegate: AnyObject {
    func serviceTermsModalScreenViewModel(_ viewModel: ServiceTermsModalScreenViewModelType, didUpdateViewState viewSate: ServiceTermsModalScreenViewState)
}

protocol ServiceTermsModalScreenViewModelCoordinatorDelegate: AnyObject {
    func serviceTermsModalScreenViewModel(_ coordinator: ServiceTermsModalScreenViewModelType, displayPolicy policy: MXLoginPolicyData)
    func serviceTermsModalScreenViewModelDidAccept(_ viewModel: ServiceTermsModalScreenViewModelType)
    func serviceTermsModalScreenViewModelDidDecline(_ viewModel: ServiceTermsModalScreenViewModelType)
}

/// Protocol describing the view model used by `ServiceTermsModalScreenViewController`
protocol ServiceTermsModalScreenViewModelType {

    var serviceUrl: String { get }
    var serviceType: MXServiceType { get }
    var policies: [MXLoginPolicyData]? { get set }
    var alreadyAcceptedPoliciesUrls: [String] { get set }
        
    var viewDelegate: ServiceTermsModalScreenViewModelViewDelegate? { get set }
    var coordinatorDelegate: ServiceTermsModalScreenViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: ServiceTermsModalScreenViewAction)
}
