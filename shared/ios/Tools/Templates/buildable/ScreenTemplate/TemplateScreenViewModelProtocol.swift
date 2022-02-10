import Foundation

protocol TemplateScreenViewModelViewDelegate: AnyObject {
    func templateScreenViewModel(_ viewModel: TemplateScreenViewModelProtocol, didUpdateViewState viewSate: TemplateScreenViewState)
}

protocol TemplateScreenViewModelCoordinatorDelegate: AnyObject {
    func templateScreenViewModel(_ viewModel: TemplateScreenViewModelProtocol, didCompleteWithUserDisplayName userDisplayName: String?)
    func templateScreenViewModelDidCancel(_ viewModel: TemplateScreenViewModelProtocol)
}

/// Protocol describing the view model used by `TemplateScreenViewController`
protocol TemplateScreenViewModelProtocol {        
        
    var viewDelegate: TemplateScreenViewModelViewDelegate? { get set }
    var coordinatorDelegate: TemplateScreenViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: TemplateScreenViewAction)
    
    var viewState: TemplateScreenViewState { get }
}
