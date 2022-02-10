import Foundation

protocol SettingsDiscoveryViewModelViewDelegate: AnyObject {
    func settingsDiscoveryViewModel(_ viewModel: SettingsDiscoveryViewModelType, didUpdateViewState viewState: SettingsDiscoveryViewState)
}

@objc protocol SettingsDiscoveryViewModelCoordinatorDelegate: AnyObject {
    func settingsDiscoveryViewModel(_ viewModel: SettingsDiscoveryViewModel, didSelectThreePidWith medium: String, and address: String)
    func settingsDiscoveryViewModelDidTapAcceptIdentityServerTerms(_ viewModel: SettingsDiscoveryViewModel)
}

protocol SettingsDiscoveryViewModelType {
    
    var viewDelegate: SettingsDiscoveryViewModelViewDelegate? { get set }
    
    var coordinatorDelegate: SettingsDiscoveryViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SettingsDiscoveryViewAction)
}
