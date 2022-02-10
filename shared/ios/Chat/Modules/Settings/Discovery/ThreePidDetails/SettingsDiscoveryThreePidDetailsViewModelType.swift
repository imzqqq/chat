// File created from ScreenTemplate
// $ createScreen.sh Details SettingsDiscoveryThreePidDetails

import Foundation

protocol SettingsDiscoveryThreePidDetailsViewModelViewDelegate: AnyObject {
    func settingsDiscoveryThreePidDetailsViewModel(_ viewModel: SettingsDiscoveryThreePidDetailsViewModelType, didUpdateViewState viewSate: SettingsDiscoveryThreePidDetailsViewState)
}

/// Protocol describing the view model used by `SettingsDiscoveryThreePidDetailsViewController`
protocol SettingsDiscoveryThreePidDetailsViewModelType {
    
    var threePid: MX3PID { get }
            
    var viewDelegate: SettingsDiscoveryThreePidDetailsViewModelViewDelegate? { get set }
    
    func process(viewAction: SettingsDiscoveryThreePidDetailsViewAction)
}
